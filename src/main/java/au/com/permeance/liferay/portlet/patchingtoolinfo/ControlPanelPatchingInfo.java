package au.com.permeance.liferay.portlet.patchingtoolinfo;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Liferay
 */
@Component(
	immediate = true,
	property = {
		"panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_CONFIGURATION,
		"service.ranking:Integer=100"
	},
	service = PanelApp.class
)
public class ControlPanelPatchingInfo extends BasePanelApp {

	@Override
	public String getPortletId() {
		return "au_com_permeance_liferay_portlet_patchingtoolinfo_PatchingToolInfoMVCPortlet";
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=au_com_permeance_liferay_portlet_patchingtoolinfo_PatchingToolInfoMVCPortlet)",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}