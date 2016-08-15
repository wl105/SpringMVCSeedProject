package com.zhangzhihao.SpringMVCSeedProject.Controller;

import com.zhangzhihao.SpringMVCSeedProject.Model.Log;
import com.zhangzhihao.SpringMVCSeedProject.Service.LogService;
import com.zhangzhihao.SpringMVCSeedProject.Utils.PageResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.zhangzhihao.SpringMVCSeedProject.Utils.LogUtils.LogToDB;

@Controller
@RequestMapping("/Log")
public class LogController {
    @Autowired
    private LogService logService;

    /*@Inject
    public LogController(LogService logService){
        Assert.assertNotNull(logService,"logService不可为Null！");
        this.logService=logService;
    }*/

    /**
     * 日志统计界面
     *
     * @return 日志统计界面
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String logPage() {
        return "Log/Log";
    }

    /**
     * 获得日志的错误信息，日志条数
     *
     * @return json数据
     */
    @RequestMapping(value = "/getLogInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Long> getLogInfo() {
        Map<String, Long> map = new HashMap<>();
        long LogUtilsCount = 0L;//Controller出了异常
        long LogAspectCount = 0L;//自定义类异常
        long totalCount = 0L;
        try {
            LogUtilsCount = logService.getExceptionCountByCallerFilename("LogUtils.java");//Controller出了异常
            LogAspectCount = logService.getExceptionCountByCallerFilename("LogAspect.java");//自定义类异常
            totalCount = logService.getExceptionCount();
        } catch (Exception e) {
            LogToDB(e);
        }
        Long otherCount = totalCount - LogAspectCount - LogUtilsCount;
        map.put("totalCount", totalCount);
        map.put("LogUtilsCount", LogUtilsCount);
        map.put("LogAspectCount", LogAspectCount);
        map.put("otherCount", otherCount);
        return map;
    }

    /**
     * 日志分页查询
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return json数据
     */
    @RequestMapping(value = "/getLogByPage/pageNumber/{pageNumber}/pageSize/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public PageResults<Log> getLogByPage(@PathVariable int pageNumber,
                                    @PathVariable int pageSize) throws Exception {
       /* try {
            return logService.getListByPage(pageNumber, pageSize);
        } catch (Exception e) {
            LogToDB(e);
            return "";
        }*/
        return logService.getListByPage(pageNumber, pageSize);
    }

}
