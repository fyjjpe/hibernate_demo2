import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by yuanjie.fang on 2018/2/27.
 */
public class SessionTest {
    @Test
    public void testOpenSession() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.openSession();//必须手动关闭
        Session session2 = sessionFactory.openSession();//必须手动关闭
        System.out.println(session==session2);


        if (session != null) {
            System.out.println("session创建成功");
        } else {
            System.out.println("创建失败");
        }

    }

    @Test
    public void testCetCurrentSession() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session = sessionFactory.getCurrentSession();//事务提交或回滚之后自动关闭
        Session session2 = sessionFactory.getCurrentSession();
        System.out.println(session == session2);

        if (session != null) {
            System.out.println("session创建成功");
        } else {
            System.out.println("创建失败");
        }
    }


    @Test
    public void testSaveStudentWithOpenSession(){
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        Session session1 = sessionFactory.openSession();//必须手动关闭
        Transaction transaction = session1.beginTransaction();
        Students s = new Students(1,"张三","男",new Date(),"苏州");
        session1.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                System.out.println("connection1:"+connection.hashCode());
            }
        });
        session1.save(s);
//        session1.close();
        transaction.commit();

        Session session2 = sessionFactory.openSession();
        transaction = session2.beginTransaction();
        s = new Students(2,"李四","男",new Date(),"苏州");
        session2.doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                System.out.println("connection2:"+connection.hashCode());
            }
        });
        session2.save(s);
        transaction.commit();

    }


}
