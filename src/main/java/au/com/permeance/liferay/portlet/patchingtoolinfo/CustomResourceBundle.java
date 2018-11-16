package au.com.permeance.liferay.portlet.patchingtoolinfo;

import com.liferay.portal.kernel.language.UTF8Control;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

@Component(immediate = true, property = "language.id=en_AU", service = ResourceBundle.class)
public class CustomResourceBundle extends ResourceBundle {

	@Override
	public Enumeration<String> getKeys() {
		return resourceBundle.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		return resourceBundle.getObject(key);
	}

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("content.Language", UTF8Control.INSTANCE);
}
