package jishun.sorm.bean;

public class Configuration {

    private String URL;

    private String user;

    private String pwd;

    private String DB;

    private String src;

    private String poPackage;
    
    private String queryClass;
    

    public String getQueryClass() {
        return queryClass;
    }

    public Configuration(String URL, String user, String pwd, String DB, String src, String poPackage,
            String queryClass) {
        super();
        this.URL = URL;
        this.user = user;
        this.pwd = pwd;
        this.DB = DB;
        this.src = src;
        this.poPackage = poPackage;
        this.queryClass = queryClass;
    }

    public String getURL() {
        return URL;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getDB() {
        return DB;
    }

    public String getSrc() {
        return src;
    }

    public String getPoPackage() {
        return poPackage;
    }

}
