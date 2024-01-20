package r.dev.listeners;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class RepositoryListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
//        Object testInstance = testResult.getInstance();
//        injectRepositoriesRecursively(testInstance);
    }



    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
//        RepositoryInjector.closeConnections();
    }
}