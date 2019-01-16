package com.example.library.configuration.quartz;

public class QuartzPropertyKeys {

   public static final String SCHEDULER_NAME = "org.quartz.scheduler.name";
   public static final String INSTANCE_NAME = "org.quartz.scheduler.instanceName";
   public static final String INSTANCE_ID = "org.quartz.scheduler.instanceId";
   public static final String SCHEDULER_UPDATE_CHECK = "org.quartz.scheduler.skipUpdateCheck";
   public static final String ISCLUSTERED = "org.quartz.jobStore.isClustered";
   public static final String CLUSTER_CHECK_IN_INTERVAL = "org.quartz.jobStore.clusterCheckinInterval";
   public static final String THREAD_POOL_CLASS = "org.quartz.threadPool.class";
   public static final String THREAD_COUNT = "org.quartz.threadPool.threadCount";
   public static final String USE_PROPERTIES = "org.quartz.jobStore.useProperties";
   public static final String JOB_STORE_CLASS = "org.quartz.jobStore.class";
   public static final String DRIVER_DELEGATE_CLASS = "org.quartz.jobStore.driverDelegateClass";
   public static final String TABLE_PREFIX = "org.quartz.jobStore.tablePrefix";
   public static final String JOB_STORE_THRESHOLD = "org.quartz.jobStore.misfireThreshold";
   
   

   private QuartzPropertyKeys() {}
   
}