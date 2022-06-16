package com.rabbit.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

public class LdapTest {
    private static Logger logger = LoggerFactory.getLogger(LdapTest.class);

    public static boolean authenticate(String userName, String password) {
        boolean bRtn = false;// 标注是否验证成功，初始为false
        Map<String, String> map = new HashMap<String, String>();
        DirContext ctx = null;
        try {
            //这条代码执行成功就是验证通过了，至于为什么我也不知道
            ctx = getContext(userName, password);
            bRtn = true;
            logger.info("Ldap验证通过!");
            if (ctx != null) {
                NamingEnumeration<NameClassPair> list = ctx.list("cn=alex,ou=Group,dc=hex,dc=com");
                while (list.hasMore()) {
                    NameClassPair ncp = list.next();
                    String cn = ncp.getName();
                    if (cn.indexOf("=") != -1) {
                        int index = cn.indexOf("=");
                        cn = cn.substring(index + 1, cn.length());
                        map.put(cn, ncp.getNameInNamespace());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Ldap 初始化 出错:", ex);
        }
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            System.out.println("Key:" + entry.getKey());
            System.out.println("Value:" + entry.getValue());
        }
        logger.info("=============================================================");
        //验证成功返回 true，验证失败返回false
        return bRtn;

    }


    public static DirContext getContext(String userName, String password) throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>(4);
        // LDAP 服务器的 URL 地址，
        String LDAP_URL = "ldap://119.29.235.242:389";
        //env 中的key都是固定值在 javax.naming.Context 类中
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// ldapCF
        env.put(Context.PROVIDER_URL, LDAP_URL);// ldapURL
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // ldapAuthMode
        //username和对应的password怎么在LDAP服务器中设置，我也不知道
        //通过默认的用户名"cn=manager,dc=aaa,dc=bbb"(aaa、bbb的具体值要在配置文件中配置，具体看参考博文)和密码"secret",可以测试连接是否成功
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = new InitialDirContext(env);
        return ctx;
    }

    public static void creatOU(String userName, String password) {
        DirContext ctx = null;
        try {
            ctx = getContext(userName, password);
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectclass");
            objclassSet.add("top");
            objclassSet.add("organizationalUnit");
            attrsbu.put(objclassSet);
            attrsbu.put("ou", "Test");
            attrsbu.put("description", "description");

            ctx.createSubcontext("ou=Test,dc=hex,dc=com", attrsbu);
            logger.info("添加部门成功");

            BasicAttributes attrs1 = new BasicAttributes();
            BasicAttribute objclass = new BasicAttribute("objectclass");
            objclass.add("top");
            objclass.add("posixGroup");
            attrs1.put(objclass);
            attrs1.put("cn", "mingjob");
            attrs1.put("gidNumber", "1000");
            // attrs1.put("memberUid","mingjob");
            ctx.createSubcontext("cn=mingjob,ou=Test,dc=hex,dc=com", attrs1);
            logger.info("添加部门组成功");


        } catch (NamingException e) {
            logger.error("Ldap 初始化 出错:", e);
        }
    }

    public static void main(String[] args) {
        LdapTest.authenticate("cn=admin,dc=hex,dc=com", "123456");
        //  LdapTest.creatOU("cn=admin,dc=hex,dc=com", "123456");
        // LdapTest.addUser("cn=admin,dc=hex,dc=com", "123456");
        LdapTest.modUser("cn=admin,dc=hex,dc=com", "123456");
    }

    public static void addUser(String userName, String password) {
        DirContext ctx = null;
        try {
            ctx = getContext(userName, password);
            BasicAttributes attrsbu = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectclass");
            objclassSet.add("inetOrgPerson");
            objclassSet.add("posixAccount");
            objclassSet.add("shadowAccount");
            attrsbu.put(objclassSet);
            attrsbu.put("sn", "tom");
            attrsbu.put("cn", "tom");
            attrsbu.put("uid", "tom");
            attrsbu.put("uidNumber", "1000");
            attrsbu.put("gidNumber", "1000");
            attrsbu.put("homeDirectory", "/home/tom");
            attrsbu.put("userPassword", "123456");
            ctx.createSubcontext("cn=tom,cn=mingjob,ou=Test,dc=hex,dc=com", attrsbu);
         /*   ModificationItem[] mods = new ModificationItem[1];
            mods[0]= new ModificationItem(DirContext.ADD_ATTRIBUTE,new BasicAttribute("uid",""));
            ctx.modifyAttributes();*/
            logger.info("添加用户成功");
        } catch (Exception e) {
            System.out.println("Exception in add():" + e);
        }
    }
    public static void delUser(String userName, String password) {
        DirContext ctx = null;
        try {
            ctx = getContext(userName, password);
            ctx.destroySubcontext("cn=app,cn=alex,ou=Group,dc=hex,dc=com");
            logger.info("删除用户成功");
        }catch (Exception e){
            logger.info("删除用户失败");
        }
    }
    public static void modUser(String userName, String password) {
        DirContext ctx = null;
        try {
            ctx = getContext(userName, password);
            ctx.rename("cn=tom,cn=alex,ou=Group,dc=hex,dc=com","cn=app,ou=People,dc=hex,dc=com");
            logger.info("修改用户成功");
        }catch (Exception e){
            logger.info("修改用户失败");
        }
    }
}

