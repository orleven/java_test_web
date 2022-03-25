package com.orleven.fastjson.breaker.data;

import com.orleven.fastjson.breaker.BlackInfo;

import java.util.LinkedList;

public class V1_2_67 {
    private static BlackInfo data;

    public static BlackInfo getData() {
        if (data == null) {
            data = new BlackInfo();
            data.version = 1267;

            data.known = new LinkedList<BlackInfo.BlockItem>() {{

                // 白名单
                add(new BlackInfo.BlockItem(956883420092542580L, "java.awt.Rectangle")); // 0xD4788669A13AE74L
                add(new BlackInfo.BlockItem(-2265617974881722705L, "java.awt.Point")); // 0xE08EE874A26F5EAFL
                add(new BlackInfo.BlockItem(-2473987886800209058L, "java.awt.Font")); // 0xDDAAA11FECA77B5EL
                add(new BlackInfo.BlockItem(829148494126372070L, "java.awt.Color")); // 0xB81BA299273D4E6L
                add(new BlackInfo.BlockItem(-6293031534589903644L, "com.alibaba.fastjson.util.AntiCollisionHashMap")); //0xA8AAA929446FFCE4L
                add(new BlackInfo.BlockItem(-3393714734093696063L, "com.alipay.sofa.rpc.core.exception.SofaTimeOutException")); //0xD0E71A6E155603C1L
                add(new BlackInfo.BlockItem(-6976602508726000783L, "java.util.Collections.UnmodifiableMap"));  // 0x9F2E20FB6049A371L
                add(new BlackInfo.BlockItem(59775428743665658L, "java.util.concurrent.ConcurrentSkipListMap")); //  0xD45D6F8C9017FAL
                add(new BlackInfo.BlockItem(7267793227937552092L, "java.util.concurrent.ConcurrentSkipListSet")); // 0x64DC636F343516DCL
//                add(new BlackInfo.BlockItem(-4837536971810737970L, "org.springframework.")); // 0xBCDD9DC12766F0CEL, ？？？
                add(new BlackInfo.BlockItem(9215131087512669423L, "org.springframework.dao.CannotAcquireLockException"));  // 0x7FE2B8E675DA0CEFL
                add(new BlackInfo.BlockItem(-520183782617964618L, "org.springframework.dao.CannotSerializeTransactionException")); //  0xF8C7EF9B13231FB6L
                add(new BlackInfo.BlockItem(4784070066737926537L, "org.springframework.dao.CleanupFailureDataAccessException"));  //0x42646E60EC7E5189L
                add(new BlackInfo.BlockItem(-3714900953609113456L, "org.springframework.dao.ConcurrencyFailureException")); // 0xCC720543DC5E7090L
                add(new BlackInfo.BlockItem(-4540135604787511831L, "org.springframework.dao.DataAccessResourceFailureException"));  //0xC0FE32B8DC897DE9L
                add(new BlackInfo.BlockItem(-2551988546877734201L, "org.springframework.dao.DataIntegrityViolationException")); // 0xDC9583F0087CC2C7L
                add(new BlackInfo.BlockItem(6073645722991901167L, "org.springframework.dao.DataRetrievalFailureException")); // 0x5449EC9B0280B9EFL
                add(new BlackInfo.BlockItem(-1477946458560579955L, "org.springframework.dao.DeadlockLoserDataAccessException")); // 0xEB7D4786C473368DL
                add(new BlackInfo.BlockItem(4960004821520561233L, "org.springframework.dao.DuplicateKeyException")); // 0x44D57A1B1EF53451L
                add(new BlackInfo.BlockItem(-3950343444501679205L, "org.springframework.dao.EmptyResultDataAccessException")); // 0xC92D8F9129AF339BL
                add(new BlackInfo.BlockItem(711449177569584898L, "org.springframework.dao.IncorrectResultSizeDataAccessException")); // 0x9DF9341F0C76702L
                add(new BlackInfo.BlockItem(-2631228350337215662L, "org.springframework.dao.IncorrectUpdateSemanticsDataAccessException")); // 0xDB7BFFC197369352L
                add(new BlackInfo.BlockItem(8357451534615459155L, "org.springframework.dao.InvalidDataAccessApiUsageException")); // 0x73FBA1E41C4C3553L
                add(new BlackInfo.BlockItem(532945107123976213L, "org.springframework.dao.InvalidDataAccessResourceUsageException")); // 0x76566C052E83815L
                add(new BlackInfo.BlockItem(7048426940343117278L, "org.springframework.dao.NonTransientDataAccessException")); // 0x61D10AF54471E5DEL
                add(new BlackInfo.BlockItem(-9013707057526259810L, "org.springframework.dao.NonTransientDataAccessResourceException")); // 0x82E8E13016B73F9EL
                add(new BlackInfo.BlockItem(-1759511109484434299L, "org.springframework.dao.OptimisticLockingFailureException")); // 0xE794F5F7DCD3AC85L
                add(new BlackInfo.BlockItem(4567982875926242015L, "org.springframework.dao.PermissionDeniedDataAccessException")); // 0x3F64BC3933A6A2DFL
                add(new BlackInfo.BlockItem(-8773806119481270567L, "org.springframework.dao.PessimisticLockingFailureException")); // 0x863D2DD1E82B9ED9L
                add(new BlackInfo.BlockItem(5454920836284873808L, "org.springframework.dao.QueryTimeoutException")); // 0x4BB3C59964A2FC50L
                add(new BlackInfo.BlockItem(6137737446243999215L, "org.springframework.dao.RecoverableDataAccessException")); // 0x552D9FB02FFC9DEFL
                add(new BlackInfo.BlockItem(2380202963256720577L, "org.springframework.dao.TransientDataAccessException")); // 0x21082DFBF63FBCC1L
                add(new BlackInfo.BlockItem(1696465274354442213L, "org.springframework.dao.TransientDataAccessResourceException")); //   //0x178B0E2DC3AE9FE5L
                add(new BlackInfo.BlockItem(2643099543618286743L, "org.springframework.dao.TypeMismatchDataAccessException")); // 0x24AE2D07FB5D7497L
                add(new BlackInfo.BlockItem(-8070393259084821111L, "org.springframework.dao.UncategorizedDataAccessException")); // 0x90003416F28ACD89L
                add(new BlackInfo.BlockItem(8331868837379820532L, "org.springframework.jdbc.BadSqlGrammarException")); // 0x73A0BE903F2BCBF4L
                add(new BlackInfo.BlockItem(8890227807433646566L, "org.springframework.jdbc.CannotGetJdbcConnectionException")); // 0x7B606F16A261E1E6L
                add(new BlackInfo.BlockItem(-5779433778261875721L, "org.springframework.jdbc.IncorrectResultSetColumnCountException")); // 0xAFCB539973CEA3F7L
                add(new BlackInfo.BlockItem(5348524593377618456L, "org.springframework.jdbc.InvalidResultSetAccessException")); //   //0x4A39C6C7ACB6AA18L
                add(new BlackInfo.BlockItem(-7043543676283957292L, "org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException")); // 0x9E404E583F254FD4L
                add(new BlackInfo.BlockItem(3804572268889088203L, "org.springframework.jdbc.LobRetrievalFailureException")); // 0x34CC8E52316FA0CBL
                add(new BlackInfo.BlockItem(-5399450433995651784L, "org.springframework.jdbc.SQLWarningException")); // 0xB5114C70135C4538L
                add(new BlackInfo.BlockItem(9166532985682478006L, "org.springframework.jdbc.UncategorizedSQLException")); // 0x7F36112F218143B6L
                add(new BlackInfo.BlockItem(2793877891138577121L, "org.springframework.cache.support.NullValue")); // 0x26C5D923AF21E2E1L
                add(new BlackInfo.BlockItem(-3378497329992063044L, "org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken")); // 0xD11D2A941337A7BCL
                add(new BlackInfo.BlockItem(5695987590363189151L, "org.springframework.security.oauth2.common.DefaultOAuth2AccessToken")); // 0x4F0C3688E8A18F9FL
                add(new BlackInfo.BlockItem(-4207865850564917696L, "org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken")); // 0xC59AA84D9A94C640L
                add(new BlackInfo.BlockItem(2238472697200138595L, "org.springframework.util.LinkedMultiValueMap")); // 0x1F10A70EE4065963L
                add(new BlackInfo.BlockItem(6160752908990493848L, "org.springframework.util.LinkedCaseInsensitiveMap")); // 0x557F642131553498L
                add(new BlackInfo.BlockItem(-8421588593326113468L, "org.springframework.remoting.support.RemoteInvocation")); // 0x8B2081CB3A50BD44L
                add(new BlackInfo.BlockItem(-6081111809668363619L, "org.springframework.remoting.support.RemoteInvocationResult")); // 0xAB9B8D073948CA9DL
                add(new BlackInfo.BlockItem(6114875255374330593L, "org.springframework.security.web.savedrequest.SavedCookie")); // 0x54DC66A59269BAE1L
                add(new BlackInfo.BlockItem(1233162291719202522L, "org.springframework.security.web.csrf.DefaultCsrfToken")); // 0x111D12921C5466DAL
                add(new BlackInfo.BlockItem(1863557081881630420L, "org.springframework.security.web.authentication.WebAuthenticationDetails")); // 0x19DCAF4ADC37D6D4L
                add(new BlackInfo.BlockItem(6939315124833099497L, "org.springframework.security.core.context.SecurityContextImpl")); // 0x604D6657082C1EE9L
                add(new BlackInfo.BlockItem(-816725787720647462L, "org.springframework.security.authentication.UsernamePasswordAuthenticationToken")); // 0xF4AA683928027CDAL
                add(new BlackInfo.BlockItem(-7858127399773263546L, "org.springframework.security.core.authority.SimpleGrantedAuthority")); // 0x92F252C398C02946L
                add(new BlackInfo.BlockItem(484499585846206473L, "org.springframework.security.core.userdetails.User")); // 0x6B949CE6C2FE009L

                // 黑名单
                add(new BlackInfo.BlockItem(-2753427844400776271L, "com.ibatis.sqlmap.engine.datasource")); //0xd9c9dbf6bbd27bb1L, 1.2.67 被删
            }};
            data.unknown = new LinkedList<BlackInfo.BlockItem>() {{
                add(new BlackInfo.BlockItem(-3077205613010077203L, ""));  // 0xD54B91CC77B239EDL
                add(new BlackInfo.BlockItem(-2825378362173150292L, ""));  // 0xD8CA3D595E982BACL

                add(new BlackInfo.BlockItem(-6025144546313590215L, ""));  //0xAC6262F52C98AA39L
                add(new BlackInfo.BlockItem(-7775351613326101303L, ""));  //0x941866E73BEFF4C9L
                add(new BlackInfo.BlockItem(-5939269048541779808L, ""));  //0xAD937A449831E8A0L
                add(new BlackInfo.BlockItem(-3975378478825053783L, ""));  //0xC8D49E5601E661A9L
                add(new BlackInfo.BlockItem(-5885964883385605994L, ""));  //0xAE50DA1FAD60A096L
                add(new BlackInfo.BlockItem(-2439930098895578154L, ""));  //0xDE23A0809A8B9BD6L
                add(new BlackInfo.BlockItem(-2378990704010641148L, ""));  //0xDEFC208F237D4104L
                add(new BlackInfo.BlockItem(-905177026366752536L, ""));  //0xF3702A4A5490B8E8L
                add(new BlackInfo.BlockItem(2660670623866180977L, ""));  //0x24EC99D5E7DC5571L
                add(new BlackInfo.BlockItem(-831789045734283466L, ""));  //0xF474E44518F26736L
                add(new BlackInfo.BlockItem(-582813228520337988L, ""));  //0xF7E96E74DFA58DBCL
                add(new BlackInfo.BlockItem(-254670111376247151L, ""));  //0xFC773AE20C827691L
                add(new BlackInfo.BlockItem(3637939656440441093L, ""));  //0x327C8ED7C8706905L
                add(new BlackInfo.BlockItem(2731823439467737506L, ""));  //0x25E962F1C28F71A2L
                add(new BlackInfo.BlockItem(3114862868117605599L, ""));  //0x2B3A37467A344CDFL
                add(new BlackInfo.BlockItem(3256258368248066264L, ""));  //0x2D308DBBC851B0D8L
                add(new BlackInfo.BlockItem(3547627781654598988L, ""));  //0x313BB4ABD8D4554CL
                add(new BlackInfo.BlockItem(5274044858141538265L, ""));  //0x49312BDAFB0077D9L
                add(new BlackInfo.BlockItem(5474268165959054640L, ""));  //0x4BF881E49D37F530L
                add(new BlackInfo.BlockItem(4254584350247334433L, ""));  //0x3B0B51ECBF6DB221L
                add(new BlackInfo.BlockItem(6854854816081053523L, ""));  //0x5F215622FB630753L
                add(new BlackInfo.BlockItem(5347909877633654828L, ""));  //0x4A3797B30328202CL
                add(new BlackInfo.BlockItem(5450448828334921485L, ""));  //0x4BA3E254E758D70DL
                add(new BlackInfo.BlockItem(5596129856135573697L, ""));  //0x4DA972745FEB30C1L
                add(new BlackInfo.BlockItem(5751393439502795295L, ""));  //0x4FD10DDC6D13821FL
                add(new BlackInfo.BlockItem(7375862386996623731L, ""));  //0x665C53C311193973L
            }};
        }
        return data;
    }
}