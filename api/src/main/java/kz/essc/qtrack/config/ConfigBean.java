//package kz.essc.qtrack.config;
//
//import kz.essc.qtrack.core.LangBean;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//
//@RequestScoped
//public class ConfigBean {
//    @Inject
//    LangBean langBean;
//
//    public ConfigWrapper translated(ConfigWrapper wrapper) {
//        try {
//            String name = "config.org.name";
//            wrapper.setOrgNameKz(langBean.getMessage(name, LangBean.Code.kz.toString()));
//            wrapper.setOrgNameEn(langBean.getMessage(name, LangBean.Code.en.toString()));
//            wrapper.setOrgNameRu(langBean.getMessage(name, LangBean.Code.ru.toString()));
//
//            String text = "config.org.text";
//            wrapper.setOrgTextKz(langBean.getMessage(text, LangBean.Code.kz.toString()));
//            wrapper.setOrgTextEn(langBean.getMessage(text, LangBean.Code.en.toString()));
//            wrapper.setOrgTextRu(langBean.getMessage(text, LangBean.Code.ru.toString()));
//        }
//        catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        return wrapper;
//    }
//}
