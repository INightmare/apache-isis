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

import org.apache.wicket.model.Model;

/**
 * Panel containing a single {@link CssMenuItem}.
 */
class CssMenuItemPanel extends CssMenuItemPanelAbstract<CssMenuItemPanel.MyModel> {

    private static final long serialVersionUID = 1L;

    static class MyModel extends Model<CssMenuItem> {
        private static final long serialVersionUID = 1L;

        public MyModel(final CssMenuItem cssMenuItem) {
            super(cssMenuItem);
        }
    }

    public CssMenuItemPanel(final String id, final CssMenuItem cssMenuItem) {
        super(id, new MyModel(cssMenuItem));

        addSubMenuItems(this, getModel().getObject());
    }
}
