package icat.app.shortlink.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class LinkStatsRecordDTO {

    private Boolean uniqueVisitor;

    private Boolean uniqueIp;

    private String ip;

    private String browser;

    private String system;

    private String device;

    private Date date;

}
