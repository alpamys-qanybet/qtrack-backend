package kz.essc.qtrack.stat;

import kz.essc.qtrack.client.Process;
import kz.essc.qtrack.sc.user.User;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestScoped
public class StatBean {
    @PersistenceContext
    EntityManager em;

    public List<Process> get(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select p from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        if (fw.getOperator() != null)
            hql.append("and p.operatorId = :operator ");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        if (fw.getOperator() != null)
            query.setParameter("operator", fw.getOperator().longValue());

        List<Process> list = query.getResultList();
//                (List) em.createQuery("select p from Process p ")
//                                .getResultList();

        return list;
    }

    public List<ProcessWrapper> countByLine(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select count(p), p.lineId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        hql.append("group by p.lineId");

        Query query = em.createQuery(hql.toString())
                        .setParameter("begin", fw.getBegin())
                        .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setCount( (Long)os[0] );
            w.setLine((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> countByOperator(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select count(p), p.operatorId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        if (fw.getOperator() != null)
            hql.append("and p.operatorId = :operator ");
        hql.append("group by p.operatorId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        if (fw.getOperator() != null)
            query.setParameter("operator", fw.getOperator().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setCount( (Long)os[0] );
            w.setOperator((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    private String formatPsqlDate(Date d) {
        int yyyy = d.getYear()+1900;
        int mm = d.getMonth()+1;
        int dd = d.getDate();

        return yyyy+"-"+decimal(mm)+"-"+decimal(dd);
    }

    private String decimal(int n) {
        if (n < 10)
            return "0"+n;
        return ""+n;
    }

    public List<ProcessWrapper> countByTime(FilterWrapper fw) {
        String begin = formatPsqlDate(fw.getBegin());
        String end = formatPsqlDate(fw.getEnd());

        StringBuilder sql = new StringBuilder();
        sql.append("select to_char(p.begin_, 'dd.mm.yy.HH24'), count(p) from cl_process p ");
        sql.append("where p.begin_::date >= '"+begin+"' AND p.begin_::date <= '"+end+"' ");
        if (fw.getLine() != null)
            sql.append("and p.line_ = " + fw.getLine().longValue() + " ");
        if (fw.getOperator() != null)
            sql.append("and p.operator_ = " + fw.getOperator().longValue() + " ");
        sql.append("group by to_char(p.begin_, 'dd.mm.yy.HH24')");

        List<Object[]> list = em.createNativeQuery(sql.toString()).getResultList();

        /*
        List<Object[]> list = em.createNativeQuery("select to_char(p.begin_, 'dd.mm.yy.HH24'), count(p) from cl_process p " +
                                    "where p.begin_::date >= '"+begin+"' AND p.begin_::date <= '"+end+"' "+
                                    "group by to_char(p.begin_, 'dd.mm.yy.HH24')")
                                    .getResultList();
        */
        /*
        select * from cl_process where begin_::date >= '2016-04-25' AND begin_::date <= '2016-04-25'
         */

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setsBegin( (String) os[0]);
            w.setCount((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> avgWaitingByLine(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select avg(p.wait), p.lineId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        hql.append("group by p.lineId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setAverage((Double) os[0]);
            w.setLine((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> avgWaitingByOperator(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select avg(p.wait), p.operatorId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        if (fw.getOperator() != null)
            hql.append("and p.operatorId = :operator ");
        hql.append("group by p.operatorId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        if (fw.getOperator() != null)
            query.setParameter("operator", fw.getOperator().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setAverage((Double) os[0]);
            w.setOperator((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> avgWaitingByTime(FilterWrapper fw) {
        String begin = formatPsqlDate(fw.getBegin());
        String end = formatPsqlDate(fw.getEnd());

        StringBuilder sql = new StringBuilder();
        sql.append("select to_char(p.begin_, 'dd.mm.yy.HH24'), avg(p.wait_)  from cl_process p ");
        sql.append("where p.begin_::date >= '"+begin+"' AND p.begin_::date <= '"+end+"' ");
        if (fw.getLine() != null)
            sql.append("and p.line_ = " + fw.getLine().longValue() + " ");
        if (fw.getOperator() != null)
            sql.append("and p.operator_ = " + fw.getOperator().longValue()+ " ");
        sql.append("group by to_char(p.begin_, 'dd.mm.yy.HH24')");

        List<Object[]> list = em.createNativeQuery(sql.toString()).getResultList();

        /*
        List<Object[]> list = em.createNativeQuery("select to_char(p.begin_, 'dd.mm.yy.HH24'), avg(p.wait_)  from cl_process p " +
                "where p.begin_::date >= '" + begin + "' AND p.begin_::date <= '" + end + "' " +
                "group by to_char(p.begin_, 'dd.mm.yy.HH24')")
                .getResultList();

        */

        /*
        select * from cl_process where begin_::date >= '2016-04-25' AND begin_::date <= '2016-04-25'
         */

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setsBegin((String) os[0]);
            w.setAverage(((BigDecimal) os[1]).doubleValue());

            wl.add(w);
        }

        return wl;
    }

    public List<User> getWorkedLineOperator(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select p.lineId, p.operatorId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        hql.append("and p.lineId = :line ");
        hql.append("group by p.lineId, p.operatorId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd())
                .setParameter("line", fw.getLine().longValue());

        List<Object[]> list = query.getResultList();

        List<Long> ids = new ArrayList<>();
        for (Object [] os: list) {
            long line = ((Long) os[0]).longValue();
            Long operator = (Long) os[1];

            ids.add(operator);
        }

        List<User> operators = new ArrayList<>();
        if (!ids.isEmpty())
            operators = em.createQuery("select o from User o " +
                            "where o.id in :ids ")
                            .setParameter("ids", ids)
                            .getResultList();
        return operators;
    }

    public List<ProcessWrapper> avgHandlingByLine(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select avg(p.handling), p.lineId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        hql.append("and p.handling != 0 ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        hql.append("group by p.lineId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setAverage((Double) os[0]);
            w.setLine((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> avgHandlingByOperator(FilterWrapper fw) {
        StringBuilder hql = new StringBuilder();
        hql.append("select avg(p.handling), p.operatorId from Process p ");
        hql.append("where p.begin >= :begin and p.begin <= :end ");
        hql.append("and p.handling != 0 ");
        if (fw.getLine() != null)
            hql.append("and p.lineId = :line ");
        if (fw.getOperator() != null)
            hql.append("and p.operatorId = :operator ");
        hql.append("group by p.operatorId");

        Query query = em.createQuery(hql.toString())
                .setParameter("begin", fw.getBegin())
                .setParameter("end", fw.getEnd());

        if (fw.getLine() != null)
            query.setParameter("line", fw.getLine().longValue());

        if (fw.getOperator() != null)
            query.setParameter("operator", fw.getOperator().longValue());

        List<Object[]> list = query.getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setAverage((Double) os[0]);
            w.setOperator((Long) os[1]);

            wl.add(w);
        }

        return wl;
    }

    public List<ProcessWrapper> avgHandlingByTime(FilterWrapper fw) {
        String begin = formatPsqlDate(fw.getBegin());
        String end = formatPsqlDate(fw.getEnd());

        StringBuilder sql = new StringBuilder();
        sql.append("select to_char(p.begin_, 'dd.mm.yy.HH24'), avg(p.handling_)  from cl_process p ");
        sql.append("where p.begin_::date >= '"+begin+"' AND p.begin_::date <= '"+end+"' ");
        sql.append("and p.handling_ <> 0 ");
        if (fw.getLine() != null)
            sql.append("and p.line_ = " + fw.getLine().longValue() + " ");
        if (fw.getOperator() != null)
            sql.append("and p.operator_ = " + fw.getOperator().longValue()+ " ");
        sql.append("group by to_char(p.begin_, 'dd.mm.yy.HH24')");

        List<Object[]> list = em.createNativeQuery(sql.toString()).getResultList();

        List<ProcessWrapper> wl = new ArrayList<>();
        for (Object [] os: list) {
            ProcessWrapper w = new ProcessWrapper();
            w.setsBegin((String) os[0]);
            w.setAverage(((BigDecimal) os[1]).doubleValue());

            wl.add(w);
        }

        return wl;
    }
}
