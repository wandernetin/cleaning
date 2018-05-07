package controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ApplicationScoped
@ManagedBean
public class ThemeBean {

    public String getApplicationTheme() {
        return "dark-hive";
    }

}
