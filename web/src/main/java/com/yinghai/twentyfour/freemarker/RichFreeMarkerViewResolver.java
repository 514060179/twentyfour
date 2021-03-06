package com.yinghai.twentyfour.freemarker;


import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;


public class RichFreeMarkerViewResolver extends AbstractTemplateViewResolver{

    /**
     * Set default viewClass
     */
    public RichFreeMarkerViewResolver() {
        setViewClass(RichFreeMarkerView.class);
    }

    /**
     * if viewName start with / , then ignore prefix.
     */
    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception{
        AbstractUrlBasedView view = super.buildView(viewName);
        // start with / ignore prefix
        if (viewName.startsWith("/")) {
            view.setUrl(viewName + getSuffix());
        }
        return view;
    }
}