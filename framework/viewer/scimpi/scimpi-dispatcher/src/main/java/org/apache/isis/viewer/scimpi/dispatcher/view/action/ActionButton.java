/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.viewer.scimpi.dispatcher.view.action;

import org.apache.log4j.Logger;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.consent.Consent;
import org.apache.isis.core.metamodel.facets.object.parseable.ParseableFacet;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAction;
import org.apache.isis.core.metamodel.spec.feature.ObjectActionParameter;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.viewer.scimpi.dispatcher.AbstractElementProcessor;
import org.apache.isis.viewer.scimpi.dispatcher.context.RequestContext;
import org.apache.isis.viewer.scimpi.dispatcher.processor.Request;
import org.apache.isis.viewer.scimpi.dispatcher.util.MethodsUtils;
import org.apache.isis.viewer.scimpi.dispatcher.view.HelpLink;

public class ActionButton extends AbstractElementProcessor {
    private static final Logger LOG = Logger.getLogger(ActionButton.class);

    @Override
    public void process(final Request request) {
        final String objectId = request.getOptionalProperty(OBJECT);
        final String methodName = request.getRequiredProperty(METHOD);
        final String forwardResultTo = request.getOptionalProperty(VIEW);
        final String forwardVoidTo = request.getOptionalProperty(VOID);
        final String forwardErrorTo = request.getOptionalProperty(ERROR);
        final String variable = request.getOptionalProperty(RESULT_NAME);
        final String scope = request.getOptionalProperty(SCOPE);
        final String buttonTitle = request.getOptionalProperty(BUTTON_TITLE);
        final String resultOverride = request.getOptionalProperty(RESULT_OVERRIDE);
        final String idName = request.getOptionalProperty(ID, methodName);
        String className = request.getOptionalProperty(CLASS);
        final boolean showMessage = request.isRequested(SHOW_MESSAGE, false);
        final String completionMessage = request.getOptionalProperty(MESSAGE);

        final ObjectAdapter object = MethodsUtils.findObject(request.getContext(), objectId);
        final String version = request.getContext().mapVersion(object);
        final ObjectAction action = MethodsUtils.findAction(object, methodName);

        final ActionContent parameterBlock = new ActionContent(action);
        request.setBlockContent(parameterBlock);
        request.processUtilCloseTag();
        final String[] parameters = parameterBlock.getParameters();
        final ObjectAdapter[] objectParameters = new ObjectAdapter[parameters.length];
        int i = 0;
        for (final ObjectActionParameter spec : action.getParameters()) {
            final ObjectSpecification typ = spec.getSpecification();
            if (parameters[i] == null) {
                objectParameters[i] = null;
            } else if (typ.getFacet(ParseableFacet.class) != null) {
                final ParseableFacet facet = typ.getFacet(ParseableFacet.class);
                objectParameters[i] = facet.parseTextEntry(null, parameters[i]);
            } else {
                objectParameters[i] = MethodsUtils.findObject(request.getContext(), parameters[i]);
            }
            i++;
        }

        if (MethodsUtils.isVisibleAndUsable(object, action) && MethodsUtils.canRunMethod(object, action, objectParameters).isAllowed()) {
            // TODO use the form creation mechanism as used in ActionForm
            write(request, object, action, parameters, objectId, version, forwardResultTo, forwardVoidTo, forwardErrorTo, variable, scope, buttonTitle, completionMessage, resultOverride, idName, className);
        }

        if (showMessage) {
            final Consent usable = action.isUsable(IsisContext.getAuthenticationSession(), object);
            if (usable.isVetoed()) {
                final String notUsable = usable.getReason();
                if (notUsable != null) {
                    if (className == null) {
                        className = "access";
                    }
                    request.appendHtml("<div class=\"" + className + "-message\" >");
                    request.appendAsHtmlEncoded(notUsable);
                    request.appendHtml("</div>");
                }
            } else {
                final Consent valid = action.isProposedArgumentSetValid(object, objectParameters);
                final String notValid = valid.getReason();
                if (notValid != null) {
                    if (className == null) {
                        className = "access";
                    }
                    request.appendHtml("<div class=\"" + className + "-message\" >");
                    request.appendAsHtmlEncoded(notValid);
                    request.appendHtml("</div>");
                }
            }
        }

        request.popBlockContent();
    }

    public static void write(final Request request, final ObjectAdapter object, final ObjectAction action, final String[] parameters, final String objectId, final String version, String forwardResultTo, String forwardVoidTo, String forwardErrorTo, final String variable, final String scope,
            String buttonTitle, final String completionMessage, final String resultOverride, final String idName, final String className) {
        final RequestContext context = request.getContext();

        buttonTitle = buttonTitle != null ? buttonTitle : action.getName();

        if (action.isVisible(IsisContext.getAuthenticationSession(), object).isVetoed()) {
            LOG.info("action not visible " + action.getName());
            return;
        }
        final Consent usable = action.isUsable(IsisContext.getAuthenticationSession(), object);
        if (usable.isVetoed()) {
            LOG.info("action not available: " + usable.getReason());
            return;
        }

        /*
         * 
         * TODO this mechanism fails as it tries to process tags - which we dont
         * need! Also it calls action 'edit' (not 'action'). Field[] fields =
         * new Field[0]; HiddenField[] hiddenFields = new HiddenField[] { new
         * HiddenField("service", serviceId), new HiddenField("method",
         * methodName), new HiddenField("view", forwardToView), variable == null
         * ? null : new HiddenField("variable", variable), };
         * Form.createForm(request, buttonTitle, fields, hiddenFields, false);
         */

        final String idSegment = idName == null ? "" : ("id=\"" + idName + "\" ");
        final String classSegment = "class=\"" + (className == null ? "action in-line" : className) + "\"";
        request.appendHtml("\n<form " + idSegment + classSegment + " action=\"action.app\" method=\"post\">\n");
        if (objectId == null) {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + OBJECT + "\" value=\"" + context.getVariable(RequestContext.RESULT) + "\" />\n");
        } else {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + OBJECT + "\" value=\"" + objectId + "\" />\n");
        }
        request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + VERSION + "\" value=\"" + version + "\" />\n");
        if (scope != null) {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + SCOPE + "\" value=\"" + scope + "\" />\n");
        }
        request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + METHOD + "\" value=\"" + action.getId() + "\" />\n");
        if (forwardResultTo != null) {
            forwardResultTo = context.fullFilePath(forwardResultTo);
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + VIEW + "\" value=\"" + forwardResultTo + "\" />\n");
        }
        if (forwardErrorTo == null) {
            forwardErrorTo = request.getContext().getResourceFile();
        }
        forwardErrorTo = context.fullFilePath(forwardErrorTo);
        request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + ERROR + "\" value=\"" + forwardErrorTo + "\" />\n");
        if (forwardVoidTo == null) {
            forwardVoidTo = request.getContext().getResourceFile();
        }
        forwardVoidTo = context.fullFilePath(forwardVoidTo);
        request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + VOID + "\" value=\"" + forwardVoidTo + "\" />\n");
        if (variable != null) {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + RESULT_NAME + "\" value=\"" + variable + "\" />\n");
        }
        if (resultOverride != null) {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + RESULT_OVERRIDE + "\" value=\"" + resultOverride + "\" />\n");
        }
        if (completionMessage != null) {
            request.appendHtml("  <input type=\"hidden\" name=\"" + "_" + MESSAGE + "\" value=\"" + completionMessage + "\" />\n");
        }

        for (int i = 0; i < parameters.length; i++) {
            request.appendHtml("  <input type=\"hidden\" name=\"param" + (i + 1) + "\" value=\"" + parameters[i] + "\" />\n");
        }
        request.appendHtml(request.getContext().interactionFields());
        request.appendHtml("  <input class=\"button\" type=\"submit\" value=\"" + buttonTitle + "\" name=\"execute\" title=\"" + action.getDescription() + "\" />");
        HelpLink.append(request, action.getDescription(), action.getHelp());
        request.appendHtml("\n</form>\n");
    }

    @Override
    public String getName() {
        return "action-button";
    }

}
