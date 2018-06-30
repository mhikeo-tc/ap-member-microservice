package com.appirio.service.member.util;

import com.appirio.service.member.dao.MemberExternalAccountsDAO;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thread runnable factory
 *
 * Created by rakeshrecharla on 10/6/15.
 */
public class ThreadRunnableFactory {

    /**
     * Logger for the class
     */
    private Logger logger = LoggerFactory.getLogger(ThreadRunnableFactory.class);

    @Getter
    @Setter
    private List<Throwable> exceptions = new ArrayList<Throwable>();

    @Getter
    @Setter
    private Map<String, Runnable> threadRunnable = new HashMap<String, Runnable>();

    private Map<String, Thread> accountThreads = new HashMap<String, Thread>();

    public ThreadRunnableFactory(MemberExternalAccountsDAO memberExternalAccountsDAO, Integer userId ) {

        threadRunnable.put("behance", new BehanceRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("bitbucket", new BitBucketRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("dribbble", new DribbleRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("github", new GithubRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("linkedin", new LinkedInRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("stackoverflow", new StackOverflowRunnable(memberExternalAccountsDAO, userId));
        threadRunnable.put("twitter", new TwitterRunnable(memberExternalAccountsDAO, userId));
    }

    Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            logger.debug(thread.getName() + " throws exception: " + ex);
            logger.debug("Cause : " + ex.getCause());
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logger.debug("Stack trace : " + sw.toString());
            logger.debug("Message : " + ex.getMessage());
            exceptions.add(ex);
        }
    };

    public void startThread(String account) {
        Thread thread = new Thread(threadRunnable.get(account));
        thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();
        accountThreads.put(account, thread);
    }

    public void joinThread(String account) throws InterruptedException {
        Thread thread = accountThreads.get(account);
        thread.join();
    }
}