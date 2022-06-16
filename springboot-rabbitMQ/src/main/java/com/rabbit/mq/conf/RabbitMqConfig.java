package com.rabbit.mq.conf;

import com.rabbit.mq.listener.topic.TopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.BatchingStrategy;
import org.springframework.amqp.rabbit.core.support.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);
    @Value("${spring.rabbitmq.addresses}")
    private String addresses;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private Boolean publisherConfirms;
    @Value("${spring.rabbitmq.publisher-returns}")
    private Boolean publisherReturns;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    public static final String EXCHANGE_A = "my-mq-directExchange_A";
    public static final String EXCHANGE_B = "my-mq-topicExchange_B";
    public static final String EXCHANGE_C = "my-mq-fanoutExchange_C";


    private final static String STRING = "string";
    public static final String QUEUE_A = "QUEUE_DIRECT_A";
    public static final String QUEUE_B = "QUEUE_TOPIC_B";
    public static final String QUEUE_C = "QUEUE_TOPIC_C";
    public static final String QUEUE_D = "QUEUE_FANOUT_C1";
    public static final String QUEUE_E = "QUEUE_FANOUT_C2";
    public static final String QUEUE_F = "QUEUE_FANOUT_C3";

    public static final String ROUTINGKEY_A = "directRoutingKey.A";
    //注意命名格式
    public static final String ROUTINGKEY_B = "topicRoutingKey.B";
    public static final String ROUTINGKEY_C = "topicRoutingKey.#";
    public static final String ROUTINGKEY_D = "topicRoutingKey.D";


    @Autowired
    private TopicListener topicListener;

    // 构建mq实例工厂
    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("=============================开始构建mq实例工厂");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        //确认消息是否发送给了目标
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    /**
     * 因为要设置回调类，所以应是prototype类型，如果是singleton类型，多次设置回调类会报错
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        /*
        当mandatory标志位设置为true时，如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，
        那么broker会调用basic.return方法将消息返还给生产者;当mandatory设置为false时，出现上述情况broker会直接将消息丢弃;通俗的讲，
        mandatory标志告诉broker代理服务器至少将消息route到一个队列中，否则就将消息return给发送者;
         */
        rabbitTemplate.setMandatory(true);//设为true使ReturnCallback生效。
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

   /* @Bean("batchQueueTaskScheduler")
    public TaskScheduler batchQueueTaskScheduler() {
        TaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        return taskScheduler;
    }

    //批量处理rabbitTemplate
    @Bean("batchQueueRabbitTemplate")
    public BatchingRabbitTemplate batchQueueRabbitTemplate(
            @Qualifier("batchQueueTaskScheduler") TaskScheduler taskScheduler) {

        //!!!重点： 所谓批量， 就是spring 将多条message重新组成一条message, 发送到mq, 从mq接受到这条message后，在重新解析成多条message

        //一次批量的数量
        int batchSize = 10;
        // 缓存大小限制,单位字节，
        // simpleBatchingStrategy的策略，是判断message数量是否超过batchSize限制或者message的大小是否超过缓存限制，
        // 缓存限制，主要用于限制"组装后的一条消息的大小"
        // 如果主要通过数量来做批量("打包"成一条消息), 缓存设置大点
        // 详细逻辑请看simpleBatchingStrategy#addToBatch()
        int bufferLimit = 1024; //1 K
        long timeout = 10000;

        //注意，该策略只支持一个exchange/routingKey
        //A simple batching strategy that supports only one exchange/routingKey
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);
        return new BatchingRabbitTemplate(batchingStrategy, taskScheduler);
    }
*/
    /**
     * 单一消费者
     *
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory());
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(2);//消费者的个数
        factory.setMaxConcurrentConsumers(2);//有消息堆积时，并发消费者的最大值
        factory.setPrefetchCount(1);//每个消费者每次监听时可拉取处理的消息数量
        //有事务时处理的消息数
        //  factory.setTxSize(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手工确认,此模式为公平分发。AUTO的话为轮询分发
        return factory;
    }

    /**
     * 多个消费者
     *
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory());
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //TODO：并发配置
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(3);
        return factory;
    }


    @Bean(name = "simpleContainer")
    public SimpleMessageListenerContainer simpleContainer(@Qualifier("topicQueueA") Queue topicQueueA, @Qualifier("topicQueueB") Queue topicQueueB) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageConverter(new Jackson2JsonMessageConverter());

        //TODO：并发配置
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(1);

        //TODO：消息确认-确认机制种类
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        Queue[] queues = new Queue[2];
        queues[0] = topicQueueA;
        queues[1] = topicQueueB;
        //指定队列监听
        container.setQueues(queues);
        container.setMessageListener(topicListener);

        return container;
    }

    //TODO:简单队列
    @Bean
    public Queue string() {
        return new Queue(STRING);
    }

    //TODO：基本消息模型构建  路由键方式转发消息，根据设置的路由键的值进行完全匹配时转发
    //TODO 队列与路由健绑定 生产者发送消息经过交换机就可以发送到与路由健绑定的队列
    @Bean
    public DirectExchange basicExchange() {
        return new DirectExchange(EXCHANGE_A, true, false);
    }

    @Bean(name = "basicQueue")
    public Queue basicQueue() {
        //队列定义为持久队列，消息持久化设置才会生效
        return new Queue(QUEUE_A, true);//持久队列
    }

    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with(ROUTINGKEY_A);
    }

    //TODO 这种模式不需要绑定RouteKey 广播模式：生产者发送消息经过交换机就可以发送到与交换机绑定的队列
    @Bean
    public Queue fanoutQueueA() {
        return new Queue(QUEUE_D);
    }

    @Bean
    public Queue fanoutQueueB() {
        return new Queue(QUEUE_E);
    }

    @Bean
    public Queue fanoutQueueC() {
        return new Queue(QUEUE_F);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_C, true, false);
    }

    @Bean
    public Binding fanoutBindingA() {
        return BindingBuilder.bind(fanoutQueueA()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBindingB() {
        return BindingBuilder.bind(fanoutQueueB()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBindingC() {
        return BindingBuilder.bind(fanoutQueueC()).to(fanoutExchange());
    }


    //TODO：并发配置-消息确认机制-listener,一个交换机发送消息到两个queue(Topic) 主题匹配方式转发消息

    @Bean(name = "topicQueueA")
    public Queue topicQueueA() {
        return new Queue(QUEUE_B, true);
    }

    @Bean(name = "topicQueueB")
    public Queue topicQueueB() {
        return new Queue(QUEUE_C, true);
    }

    //路由键通配符模式,符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_B, true, false);
    }

    @Bean
    public Binding topicBindingA() {
        return BindingBuilder.bind(topicQueueA()).to(topicExchange()).with(ROUTINGKEY_B);
    }

    @Bean
    public Binding topicBindingB() {
        return BindingBuilder.bind(topicQueueB()).to(topicExchange()).with(ROUTINGKEY_C);
    }


}
