/*******************************************************************************
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.google.appengine.eclipse.wtp.properties.ui;

import com.google.appengine.eclipse.core.properties.ui.DeployComponent;
import com.google.appengine.eclipse.wtp.AppEnginePlugin;
import com.google.appengine.eclipse.wtp.utils.ProjectUtils;
import com.google.gdt.eclipse.core.StatusUtilities;
import com.google.gdt.eclipse.core.ui.AbstractProjectPropertyPage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * GAE WTP project deployment property page.
 */
public final class DeployPropertiesPage extends AbstractProjectPropertyPage {
  public static final String ID = AppEnginePlugin.PLUGIN_ID + ".deployProperties";

  private DeployComponent deployComponent = new DeployComponent();
  private String currentAppId;
  private String currentVersion;

  @Override
  protected Control createContents(Composite parent) {
    Dialog.applyDialogFont(parent);
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout());
    deployComponent.createContents(composite);
    deployComponent.setModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        validateInput();
        deployComponent.setEnabled(true);
      }
    });

    initializeValues();
    return composite;
  }

  @Override
  protected void saveProjectProperties() throws Exception {
    // save App ID & Version into appengine-web.xml
    IProject project = getProject();
    String appId = deployComponent.getAppId();
    if (!appId.equals(currentAppId)) {
      ProjectUtils.setAppId(project, appId, true);
    }
    String version = deployComponent.getVersion();
    if (!version.equals(currentVersion)) {
      ProjectUtils.setAppVersion(project, version, true);
    }
  }

  /**
   * Setup current values.
   */
  private void initializeValues() {
    try {
      IProject project = getProject();
      currentAppId = ProjectUtils.getAppId(project);
      currentVersion = ProjectUtils.getAppVersion(project);
      deployComponent.setAppIdText(currentAppId);
      deployComponent.setVersionText(currentVersion);
    } catch (CoreException e) {
      AppEnginePlugin.logMessage(e);
    }
  }

  /**
   * Do validate app ID.
   */
  private IStatus validateAppId() throws CoreException {
    String enteredAppId = deployComponent.getAppId();
    if (enteredAppId.length() > 0) {
      IFile appEngineWebXml = ProjectUtils.getAppEngineWebXml(getProject());
      if (!appEngineWebXml.exists()) {
        return StatusUtilities.newErrorStatus(
            "Cannot set application ID (appengine-web.xml is missing)", AppEnginePlugin.PLUGIN_ID);
      }
    } else {
      return StatusUtilities.newWarningStatus(
          "You won't be able to deploy to Google without valid Application ID.",
          AppEnginePlugin.PLUGIN_ID);
    }
    return StatusUtilities.OK_STATUS;
  }

  /**
   * Do field values validation.
   */
  private void validateInput() {
    try {
      IStatus appIdStatus = validateAppId();
      IStatus versionStatus = validateVersion();
      updateStatus(appIdStatus, versionStatus);
    } catch (CoreException e) {
      updateStatus(StatusUtilities.newErrorStatus(e, AppEnginePlugin.PLUGIN_ID));
    }
  }

  /**
   * Do validate app version.
   */
  private IStatus validateVersion() throws CoreException {
    String enteredVersion = deployComponent.getVersion();
    if (!enteredVersion.matches("[a-zA-Z0-9-]*")) {
      return StatusUtilities.newErrorStatus(
          "Invalid version number. Only letters, digits and hyphen allowed.",
          AppEnginePlugin.PLUGIN_ID);
    }
    IFile appEngineWebXml = ProjectUtils.getAppEngineWebXml(getProject());
    if (enteredVersion.length() > 0 && !appEngineWebXml.exists()) {
      return StatusUtilities.newErrorStatus("Cannot set version (appengine-web.xml is missing)",
          AppEnginePlugin.PLUGIN_ID);
    }
    return StatusUtilities.OK_STATUS;
  }
}
