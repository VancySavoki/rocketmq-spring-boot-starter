package com.github.rocketmq.aspect;

import com.github.rocketmq.annotation.OrderMessage;
import com.github.rocketmq.annotation.RockerMessage;
import com.github.rocketmq.annotation.TransactionMessage;
import com.github.rocketmq.autoconfigure.RocketProperties;
import com.github.rocketmq.core.factory.ThreadPoolFactory;
import com.github.rocketmq.core.utils.AspectUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: ThierrySquirrel <br/>
 * Description: <br/>
 * date: 2019/4/28 21:07<br/>
 *
 * @author Thierry<br />
 * @since JDK 1.8
 */
@Aspect
@Slf4j
@Data
public class RockerAspect {
	private RocketProperties rocketProperties;
	private ThreadPoolExecutor threadPoolExecutor;

	public RockerAspect(RocketProperties rocketProperties) {
		this.rocketProperties = rocketProperties;
		this.threadPoolExecutor = ThreadPoolFactory.createProducerThreadPoolExecutor(rocketProperties);
	}

	@Pointcut("@annotation(com.github.rocketmq.annotation.RockerMessage)")
	public void rockerMessagePointcut() {

	}

	@Pointcut("@annotation(com.github.rocketmq.annotation.OrderMessage)")
	public void orderMessagePointcut() {

	}

	@Pointcut("@annotation(com.github.rocketmq.annotation.TransactionMessage)")
	public void transactionMessagePointcut() {

	}

	@Around("rockerMessagePointcut()")
	public Object rockerMessageSend(ProceedingJoinPoint point) throws Throwable {
		return AspectUtils.intercept(point, rocketProperties, threadPoolExecutor, RockerMessage.class);
	}

	@Around("orderMessagePointcut()")
	public Object orderMessageSend(ProceedingJoinPoint point) throws Throwable {
		return AspectUtils.intercept(point, rocketProperties, threadPoolExecutor, OrderMessage.class);
	}

	@Around("transactionMessagePointcut()")
	public Object transactionMessageSend(ProceedingJoinPoint point) throws Throwable {
		return AspectUtils.intercept(point, rocketProperties, threadPoolExecutor, TransactionMessage.class);
	}
}