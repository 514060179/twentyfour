package com.yinghai.twentyfour.aop;

import com.yinghai.twentyfour.common.util.ResponseVo;
import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class AccessTokenAop {
//    @Autowired
//    private TaxigoAccessTokensService taxigoAccessTokensService;

    private Logger log = Logger.getLogger(this.getClass());


    public Object execute(ProceedingJoinPoint pjp){
        String methodName = pjp.getSignature().getName();
        String className=pjp.getTarget().getClass().getSimpleName();
        Object result = null;
        Object[] o = pjp.getArgs();
        log.info("======>The request method " + methodName + " begins with " + Arrays.asList(pjp.getArgs()));
        try {
			result = pjp.proceed();
		} catch (Throwable e) {
            if(e instanceof UnauthorizedException){
                System.out.println("没有权限！");
            }
            HttpServletResponse response = null;
            HttpServletRequest request = null;
            for (int i = 0 ; i<o.length ;i++){
                if(o[i] instanceof HttpServletResponse){
                    response = (HttpServletResponse)o[i];
                    break;
                }
                if(o[i] instanceof HttpServletRequest){
                    request = (HttpServletRequest)o[i];
                    break;
                }
            }
            if (response!=null){
                ResponseVo.send111Code(response,"系统异常！");
            }
            if (request!=null){
                try {
                    request.setCharacterEncoding("UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
			// TODO Auto-generated catch block
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = sdf.format(new Date());
			log.error("time:"+date+" Exception:"+className+"/"+methodName+" msg:"+e);
			e.printStackTrace();
		}
        log.info("======>The request method " + methodName + " end with massage:"+result );
        return result;
    }
}
