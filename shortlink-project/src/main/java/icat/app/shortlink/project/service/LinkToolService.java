package icat.app.shortlink.project.service;

/**
 * 短链接相关工具接口
 */
public interface LinkToolService {

    /**
     * 通过Url获取网站标题
     * @param url 目标网站url
     * @return 网站标题
     */
    String getTitleByUrl(String url);

}
