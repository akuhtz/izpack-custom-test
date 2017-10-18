package com.izforge.izpack.panels.finish;

import java.io.File;

import javax.swing.JLabel;

import com.izforge.izpack.api.GuiId;
import com.izforge.izpack.api.data.Panel;
import com.izforge.izpack.api.resource.Resources;
import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.gui.log.Log;
import com.izforge.izpack.installer.data.GUIInstallData;
import com.izforge.izpack.installer.data.UninstallDataWriter;
import com.izforge.izpack.installer.gui.InstallerFrame;
import com.izforge.izpack.panels.simplefinish.SimpleFinishPanel;

public class MyFinishPanel extends SimpleFinishPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final UninstallDataWriter myUninstallDataWriter;
	private final Log myLog;

	public MyFinishPanel(Panel panel, InstallerFrame parent, GUIInstallData installData, Resources resources,
			UninstallDataWriter uninstallDataWriter, Log log) {
		super(panel, parent, installData, resources, uninstallDataWriter, log);
		// TODO Auto-generated constructor stub
		myUninstallDataWriter = uninstallDataWriter;
		myLog = log;
	}

	
    /**
     * Called when the panel becomes active.
     */
    public void panelActivate()
    {
        parent.lockNextButton();
        parent.lockPrevButton();
        parent.setQuitButtonText(getString("FinishPanel.done"));
        parent.setQuitButtonIcon("done");
        if (this.installData.isInstallSuccess())
        {

            // We set the information
            add(LabelFactory.create(parent.getIcons().get("check")));
            add(IzPanelLayout.createVerticalStrut(5));
            JLabel jLabel = LabelFactory.create(getString("FinishPanel.success"),
                                                parent.getIcons().get("preferences"), LEADING);
            jLabel.setName(GuiId.SIMPLE_FINISH_LABEL.id);
            add(jLabel, NEXT_LINE);
            add(new JLabel("This is my custom label."), NEXT_LINE);
            add(IzPanelLayout.createVerticalStrut(5));
            if (myUninstallDataWriter.isUninstallRequired())
            {
                // We prepare a message for the uninstaller feature
                String path = translatePath(installData.getInfo().getUninstallerPath());

                JLabel uninstallJLabel = LabelFactory.create(getString("FinishPanel.uninst.info"),
                                                             parent.getIcons().get("preferences"), LEADING);
                uninstallJLabel.setName(GuiId.SIMPLE_FINISH_UNINSTALL_LABEL.id);
                add(uninstallJLabel, NEXT_LINE);
                
                add(new JLabel("This is my 2nd custom label."), NEXT_LINE);
                
                add(LabelFactory.create(path, parent.getIcons().get("empty"),
                                        LEADING), NEXT_LINE);
            }
        }
        else
        {
            add(LabelFactory.create(getString("FinishPanel.fail"),
                                    parent.getIcons().get("stop"), LEADING));
        }
        getLayoutHelper().completeLayout(); // Call, or call not?
        myLog.informUser();
    }

    /**
     * Translates a relative path to a local system path.
     *
     * @param destination The path to translate.
     * @return The translated path.
     */
    private String translatePath(String destination)
    {
        // Parse for variables
        destination = installData.getVariables().replace(destination);

        // Convert the file separator characters
        return destination.replace('/', File.separatorChar);
    }

}
