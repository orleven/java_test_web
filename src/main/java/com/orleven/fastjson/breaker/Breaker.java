package com.orleven.fastjson.breaker;

import java.io.IOException;

public class Breaker {
    public static void main(String[] args) throws IOException {

        // 功能1：完善已知列表
        // 使用指定的class去完善
        String []  packs = {
//                "java.net.Inet4Address",
                "org.apache.commons.configuration2.JNDIConfiguration",
//                "java.awt.Rectangle",
//                "java.awt.Point",
//                "java.awt.Font",
//                "java.awt.Color",
//
//                "com.alibaba.fastjson.util.AntiCollisionHashMap",
//
//                "com.alipay.sofa.rpc.core.exception.SofaTimeOutException",
//                "java.util.Collections.UnmodifiableMap",
//                "java.util.concurrent.ConcurrentSkipListMap",
//                "java.util.concurrent.ConcurrentSkipListSet",
//
//                "org.springframework.dao.CannotAcquireLockException",
//                "org.springframework.dao.CannotSerializeTransactionException",
//                "org.springframework.dao.CleanupFailureDataAccessException",
//                "org.springframework.dao.ConcurrencyFailureException",
//                "org.springframework.dao.DataAccessResourceFailureException",
//                "org.springframework.dao.DataIntegrityViolationException",
//                "org.springframework.dao.DataRetrievalFailureException",
//                "org.springframework.dao.DeadlockLoserDataAccessException",
//                "org.springframework.dao.DuplicateKeyException",
//                "org.springframework.dao.EmptyResultDataAccessException",
//                "org.springframework.dao.IncorrectResultSizeDataAccessException",
//                "org.springframework.dao.IncorrectUpdateSemanticsDataAccessException",
//                "org.springframework.dao.InvalidDataAccessApiUsageException",
//                "org.springframework.dao.InvalidDataAccessResourceUsageException",
//                "org.springframework.dao.NonTransientDataAccessException",
//                "org.springframework.dao.NonTransientDataAccessResourceException",
//                "org.springframework.dao.OptimisticLockingFailureException",
//                "org.springframework.dao.PermissionDeniedDataAccessException",
//                "org.springframework.dao.PessimisticLockingFailureException",
//                "org.springframework.dao.QueryTimeoutException",
//                "org.springframework.dao.RecoverableDataAccessException",
//                "org.springframework.dao.TransientDataAccessException",
//                "org.springframework.dao.TransientDataAccessResourceException",
//                "org.springframework.dao.TypeMismatchDataAccessException",
//                "org.springframework.dao.UncategorizedDataAccessException",
//
//                "org.springframework.jdbc.BadSqlGrammarException",
//                "org.springframework.jdbc.CannotGetJdbcConnectionException",
//                "org.springframework.jdbc.IncorrectResultSetColumnCountException",
//                "org.springframework.jdbc.InvalidResultSetAccessException",
//                "org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException",
//                "org.springframework.jdbc.LobRetrievalFailureException",
//                "org.springframework.jdbc.SQLWarningException",
//                "org.springframework.jdbc.UncategorizedSQLException",
//
//                "org.springframework.cache.support.NullValue",
//
//                "org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken",
//                "org.springframework.security.oauth2.common.DefaultOAuth2AccessToken",
//                "org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken",
//
//                "org.springframework.util.LinkedMultiValueMap",
//                "org.springframework.util.LinkedCaseInsensitiveMap",
//
//                "org.springframework.remoting.support.RemoteInvocation",
//                "org.springframework.remoting.support.RemoteInvocationResult",
//
//                "org.springframework.security.web.savedrequest.DefaultSavedRequest",
//                "org.springframework.security.web.savedrequest.SavedCookie",
//                "org.springframework.security.web.csrf.DefaultCsrfToken",
//                "org.springframework.security.web.authentication.WebAuthenticationDetails",
//
//                "org.springframework.security.core.context.SecurityContextImpl",
//                "org.springframework.security.authentication.UsernamePasswordAuthenticationToken",
//                "org.springframework.security.core.authority.SimpleGrantedAuthority",
//                "org.springframework.security.core.userdetails.User",
        };

        BreakerUtils.completeDatabase(packs);

        // 使用指定的jar去完善
//        BreakerUtils.completeDatabase(new File("C:\\Users\\leadroyal\\.gradle\\caches\\modules-2\\files-2.1\\com.alibaba\\fastjson\\1.2.24\\a2b82688715ee16d874d90229d204daf3efcac8e\\fastjson-1.2.24.jar"));
        // 使用指定的目录去完善
//        BreakerUtils.completeDatabase(new File("C:\\Users\\leadroyal\\.gradle\\caches\\modules-2\\files-2.1\\"), true);


        // 功能2：输入版本号，输出已知和未知的列表
//        BreakerUtils.listDatabase(1242);
//        BreakerUtils.listDatabase();

        // 功能3：输入classname，输出被ban情况
        BreakerUtils.isBanned("ooracle.jdbc.connector.OracleManagedConnectionFactory");

    }
}
