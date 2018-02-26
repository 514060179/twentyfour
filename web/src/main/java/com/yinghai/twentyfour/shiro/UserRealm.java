package com.yinghai.twentyfour.shiro;

import com.yinghai.twentyfour.backstage.model.ManagerUser;
import com.yinghai.twentyfour.backstage.service.ManagerUserService;
import com.yinghai.twentyfour.backstage.service.TfPermissionService;
import com.yinghai.twentyfour.backstage.service.TfRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/20.
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private ManagerUserService managerUserService;
    @Autowired
    private TfPermissionService tfPermissionService;
    @Autowired
    private TfRoleService tfRoleService;
    /**
     * 权限授权函数,查詢用戶的所擁有的權限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();

        ManagerUser managerUser = (ManagerUser)SecurityUtils.getSubject().getPrincipal();
        // 取得用户的所有权限
        Set<String> permissions = new HashSet<String>();
        Set<String> roleNames = new HashSet<String>();
        permissions = tfPermissionService.findPermissionByUserId(managerUser.getId());
        roleNames = tfRoleService.findRoleByUserId(managerUser.getId());
        info.setStringPermissions(permissions);
        info.setRoles(roleNames);
        return info;
    }
    /**
     * 身份认证函数
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        //获取基于用户名和密码的令牌
        //实际上这个authcToken是从LoginController里面currentUser.login(token)传过来的
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        Session session = getSession();

        String username = token.getUsername();
        ManagerUser member = managerUserService.findByName(username);
        if(member != null){
//            if(member.getIslock() !=null && member.getIslock() == 1){
//                throw new AuthenticationException("msg:该已帐号禁止登录.");
//            }
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(member, member.getPassword().toCharArray(), ByteSource.Util.bytes(username),this.getName());
            this.setSession("currentUser", member.getUsername());

            return authcInfo;
        }
        return null;
    }
    /**
     * 保存登录名
     */
    private void setSession(Object key, Object value){
        Session session = getSession();
        System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
        if(null != session){
            session.setAttribute(key, value);
        }
    }
    private Session getSession(){
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null){
                session = subject.getSession();
            }
            if (session != null){
                return session;
            }
        }catch (InvalidSessionException e){

        }
        return null;
    }

    public static void main(String[] args) {
        String algorithmName = "md5";
        String username = "simon";
        String password = "111";

        String salt1 = username;
//        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 1;
        SimpleHash hash = new SimpleHash(algorithmName, password,
                null, hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(encodedPassword);



        int hashIterations1 = 2;//加密的次数
        Object salt = null;//盐值
        Object credentials = "111".toCharArray();//密码
        String hashAlgorithmName = "MD5";//加密方式
        Object simpleHash = new SimpleHash(hashAlgorithmName, credentials,
                salt, hashIterations1);
        System.out.println("加密后的值----->" + simpleHash);//ea86bb6ddec9c18cd45e762d1d3495e9
        //ea86bb6ddec9c18cd45e762d1d3495e9 c2ltb24=
        System.out.println(new SimpleHash("md5", credentials, ByteSource.Util.bytes("simon"), hashIterations1));//86016a1bdbac2f4e488e0a201fb31409
    }
}
