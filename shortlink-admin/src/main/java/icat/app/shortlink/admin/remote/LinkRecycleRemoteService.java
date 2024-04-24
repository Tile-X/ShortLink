package icat.app.shortlink.admin.remote;


import icat.app.shortlink.admin.remote.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecycleMoveToDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecyclePageReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecycleRecoverDTO;
import icat.app.shortlink.common.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "link-recycle-service", url = "http://localhost:8000")
public interface LinkRecycleRemoteService {

    /**
     * 将指定短链接移入回收站
     */
    @PostMapping("/link-bin")
    Result moveToLinkRecycleBin(@RequestBody LinkRecycleMoveToDTO requestParam);

    /**
     * 分页查询回收站内的短链接
     */
    @GetMapping("/link-bin")
    Result getLinkRecycleBinPage(@RequestBody LinkRecyclePageReqDTO requestParam);

    /**
     * 将指定短链接从回收站移出
     */
    @PostMapping("/link-bin/recover")
    Result recoverFromLinkRecycleBin(@RequestBody LinkRecycleRecoverDTO requestParam);

    /**
     * 删除回收站内的指定短链接
     */
    @DeleteMapping("/link-bin")
    Result deleteFromLinkRecycleBin(@RequestBody LinkDeleteReqDTO requestParam);

}
