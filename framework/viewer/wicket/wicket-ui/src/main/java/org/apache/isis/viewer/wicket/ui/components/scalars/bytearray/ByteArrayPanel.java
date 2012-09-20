/*
 * Copyright 2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.isis.viewer.wicket.ui.components.scalars.bytearray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.isis.applib.content.ContentDescription;
import org.apache.isis.applib.content.DefaultContentDescription;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.progmodel.facets.properties.content.ContentDescriptionFacetViaMethod;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.components.scalars.ScalarPanelAbstract;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Duration;

/**
 *
 * @author giedrius
 */
public class ByteArrayPanel extends ScalarPanelAbstract {
    
    private static final String ID_SCALAR_IF_REGULAR = "scalarIfRegular";
    private static final String ID_SCALAR_IF_DISABLED = "scalarIfDisabled";
    private static final String ID_SCALAR_IF_COMPACT = "scalarIfCompact";
    private static final String ID_SCALAR_NAME = "scalarName";
    private static final String ID_SCALAR_VALUE = "scalarValue";
    private static final String ID_FEEDBACK = "feedback";
    
    private static final Logger LOG = Logger.getLogger(ByteArrayPanel.class);
    
    public ByteArrayPanel(final String id, final ScalarModel model) {
        super(id, model);
    }
    
    @Override
    protected FormComponentLabel addComponentForRegular() {
        final FileUploadField fileUploadField = createFileUploadField(ID_SCALAR_VALUE);
        fileUploadField.setLabel(Model.of(getModel().getName()));
        
        final FormComponentLabel scalarIfRegular = new FormComponentLabel(ID_SCALAR_IF_REGULAR, fileUploadField);
        scalarIfRegular.add(fileUploadField);

        final Label scalarName = new Label(ID_SCALAR_NAME, getRendering().getLabelCaption(fileUploadField));
        scalarIfRegular.add(scalarName);
        scalarIfRegular.add(createResourceLink(ID_SCALAR_IF_DISABLED));
        
        addOrReplace(scalarIfRegular);
        addOrReplace(new ComponentFeedbackPanel(ID_FEEDBACK, fileUploadField));
        
        return scalarIfRegular;
    }

    @Override
    protected Component addComponentForCompact() {
        final ResourceLink resourceLink = createResourceLink(ID_SCALAR_IF_COMPACT);
        addOrReplace(resourceLink);
        return resourceLink;
    }

    protected FileUploadField createFileUploadField(String componentId) {
        
        final FileUploadField fileUploadField = new FileUploadField(componentId, new ListModel<FileUpload>() {

            @Override
            public void setObject(final List<FileUpload> fileUploads) {
                if (fileUploads == null || fileUploads.isEmpty()) {
                    return;
                }
                
                final ScalarModel model = getModel();
                final byte[] content = readUploadedContent(fileUploads.get(0));
                final ObjectAdapter adapter = getAdapterManager().adapterFor(content);
                getModel().setObject(adapter);
            }
            
        });
        return fileUploadField;
    }
    
    private byte[] readUploadedContent(FileUpload fileUpload) {
        byte[] content = null;
        try {
            content = IOUtils.toByteArray(fileUpload.getInputStream());
        } catch (IOException exception) {
            LOG.error("Error while reading uploaded content", exception);
        }
        
        return content;
    }
    
    private ResourceLink createResourceLink(String id) {
        final ContentDescription description = getContentDescription(getModel());
        final String filename = description.getName();
      
        final ResourceStreamResource resource = new ResourceStreamResource(new AbstractResourceStream() {

            @Override
            public InputStream getInputStream() throws ResourceStreamNotFoundException {
                return new ByteArrayInputStream( (byte[]) getModel().getObject().getObject());
            }

            @Override
            public void close() throws IOException {
            }

        });
        
        resource.setFileName(filename);
        resource.setCacheDuration(Duration.NONE);
        
        final ResourceLink resourceLink = new ResourceLink(id, resource);
        return resourceLink;
    }
    
    protected void onBeforeRenderWhenViewMode() {
        FormComponentLabel formComponentLabel = (FormComponentLabel) getComponentForRegular();
        formComponentLabel.get(ID_SCALAR_IF_DISABLED).setVisible(true);
        formComponentLabel.get(ID_SCALAR_VALUE).setVisible(false);
    }

    protected void onBeforeRenderWhenDisabled(final String disableReason) {
        FormComponentLabel formComponentLabel = (FormComponentLabel) getComponentForRegular();
        formComponentLabel.get(ID_SCALAR_IF_DISABLED).setVisible(true);
        formComponentLabel.get(ID_SCALAR_VALUE).setVisible(false);
    }

    protected void onBeforeRenderWhenEnabled() {
        FormComponentLabel formComponentLabel = (FormComponentLabel) getComponentForRegular();
        formComponentLabel.get(ID_SCALAR_IF_DISABLED).setVisible(false);
        formComponentLabel.get(ID_SCALAR_VALUE).setVisible(true);
    }
    
    private ContentDescription getContentDescription(final ScalarModel model) {
        ContentDescriptionFacetViaMethod contentDescriptionFacet = 
                                model.getFacet(ContentDescriptionFacetViaMethod.class);
        
        if (contentDescriptionFacet == null) {
            return new DefaultContentDescription("default", "application/octet-stream");
        } else {
            // Need to call this on the parent, as the model is the byte array
            return contentDescriptionFacet.getContentDescription(model.getParent());
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
    }
}
