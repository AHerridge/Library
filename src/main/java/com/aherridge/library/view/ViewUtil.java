package com.aherridge.library.view;

import com.aherridge.library.contracts.ContractService;
import com.aherridge.library.permissions.AdminService;
import com.aherridge.library.user.UserUtil;
import com.aherridge.library.util.Path;
import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import java.time.LocalDate;
import java.util.Map;

public class ViewUtil
{
	public static String render(Request request, Map<String, Object> model, String templatePath)
	{
		model.put("WebPath", Path.Web.class);
		model.put("user", UserUtil.getCurrentUser(request));
		model.put("date", LocalDate.now());
		model.put("contractService", ContractService.class);
		model.put("adminService", AdminService.class);
		return strictVelocityEngine().render(new ModelAndView(model, templatePath));
	}

	private static VelocityTemplateEngine strictVelocityEngine()
	{
		VelocityEngine configuredEngine = new VelocityEngine();
		configuredEngine.setProperty("runtime.references.strict", true);
		configuredEngine.setProperty("resource.loader", "class");
		configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		return new VelocityTemplateEngine(configuredEngine);
	}
}
