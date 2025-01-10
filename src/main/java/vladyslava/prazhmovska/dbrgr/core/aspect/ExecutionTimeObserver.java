package vladyslava.prazhmovska.dbrgr.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ExecutionTimeObserver {

    @Around("@annotation(vladyslava.prazhmovska.dbrgr.core.aspect.ObserveExecutionTime)")
    public Object ignoreTestDataAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = getMethod(proceedingJoinPoint);
        long executionTime = 0;
        try {
            executionTime = System.nanoTime();
            Object pointResult = proceedingJoinPoint.proceed();
            executionTime = System.nanoTime() - executionTime;
            return pointResult;
        } finally {
            log.info(
                    "Method {} of class {} execution time is: {}. Comment: {}",
                    method.getName(),
                    method.getDeclaringClass(),
                    executionTime,
                    getComment(proceedingJoinPoint)
            );
        }
    }

    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        return ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
    }

    private String getComment(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        ObserveExecutionTime annotation = signature.getMethod().getAnnotation(ObserveExecutionTime.class);
        return annotation.comment();
    }
}
