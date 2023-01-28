package ru.taustudio.duckview.control.screenshotcontrol.start;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StartController {
  @Qualifier("eurekaClient")
  @Autowired
  EurekaClient eurekaClient;

  @GetMapping
  String callStart(Model model){
    Set<String> applicationNames = new HashSet<>();
    for (Application app: eurekaClient.getApplications().getRegisteredApplications()){
      if (!"CONTROL-APP".equals(app.getName())){
      applicationNames.add(app.getName());
      }
    }
    model.addAttribute("applicationNames", applicationNames);
    return "start";
  }
}
