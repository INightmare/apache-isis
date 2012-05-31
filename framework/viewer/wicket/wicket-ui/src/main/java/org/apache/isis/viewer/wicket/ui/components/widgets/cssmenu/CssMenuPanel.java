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

package org.apache.isis.viewer.wicket.ui.components.widgets.cssmenu;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.util.ListModel;

import org.apache.isis.viewer.wicket.model.util.Strings;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;
import org.apache.isis.viewer.wicket.ui.util.CssClassAppender;

/**
 * Top level panel for a CSS menu, consisting of a number of unparented
 * {@link CssMenuItem}s.
 * 
 * <p>
 * The {@link Style} enum allows the presentation to be altered.
 */
public class CssMenuPanel extends PanelAbstract<CssMenuPanel.MyModel> {

    private static final long serialVersionUID = 1L;

    public enum Style {
        REGULAR {
            @Override
            public String getAppendValue() {
                return null; // ie, append nothing
            }
        },
        SMALL {
            @Override
            public String getAppendValue() {
                return toString();
            }
        };
        @Override
        public String toString() {
            return Strings.toCamelCase(name());
        }

        public String getAppendValue() {
            return toString();
        }
    }

    static class MyModel extends ListModel<CssMenuItem> {

        private static final long serialVersionUID = 1L;

        public MyModel(final List<CssMenuItem> cssMenuItems) {
            super(cssMenuItems);
        }
    }

    public static CssMenuItem.Builder newMenuItem(final String name) {
        return CssMenuItem.newMenuItem(name);
    }

    private final StyleAppender styleAppender;
    static final String ID_MENU_ITEMS = "menuItems";
    static final String ID_MENU_ITEM = "menuItem";

    public CssMenuPanel(final String id, final Style style, final List<CssMenuItem> topLevelMenuItems) {
        super(id, new MyModel(topLevelMenuItems));
        this.styleAppender = new StyleAppender(style);

        add(styleAppender);

        final RepeatingView menuItemRv = new RepeatingView(CssMenuPanel.ID_MENU_ITEMS);
        add(menuItemRv);

        for (final CssMenuItem cssMenuItem : this.getModel().getObject()) {
            final WebMarkupContainer menuItemMarkup = new WebMarkupContainer(menuItemRv.newChildId());
            menuItemRv.add(menuItemMarkup);

            menuItemMarkup.add(new CssMenuItemPanel(CssMenuPanel.ID_MENU_ITEM, cssMenuItem));
        }

    }

    public CssMenuPanel(final ComponentType componentType, final Style style, final CssMenuItem... topLevelMenuItems) {
        this(componentType.getWicketId(), style, Arrays.asList(topLevelMenuItems));
    }

    static final class StyleAppender extends CssClassAppender {

        private static final long serialVersionUID = 1L;

        public StyleAppender(final Style style) {
            super(style.getAppendValue());
        }

    }

}
