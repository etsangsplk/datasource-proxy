package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import net.ttddyy.dsproxy.proxy.ConnectionInvocationHandler;
import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;
import net.ttddyy.dsproxy.proxy.ProxyJdbcObject;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * @author Tadaya Tsuyukubo
 */
public class StatementInvocationHandlerMockTest {

    private static final String DS_NAME = "myDS";

    @BeforeMethod
    private void setup() {
    }

    @Test
    public void testExecuteUpdate() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query)).thenReturn(100);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);
        int result = statment.executeUpdate(query);

        assertThat(result, is(100));
        verify(stat).executeUpdate(query);
        verifyListener(listener, "executeUpdate", query, query);
    }

    @Test
    public void testExecuteUpdateForException() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.executeUpdate(query);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).executeUpdate(query);
        verifyListenerForException(listener, "executeUpdate", query, query);
    }

    @Test
    public void testExecuteUpdateWithAutoGeneratedKeys() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(100);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        int result = statment.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        assertThat(result, is(100));
        verify(stat).executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        verifyListener(listener, "executeUpdate", query, query, Statement.RETURN_GENERATED_KEYS);
    }

    @Test
    public void testExecuteUpdateWithAutoGeneratedKeysForException() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        verifyListenerForException(listener, "executeUpdate", query, query, Statement.RETURN_GENERATED_KEYS);
    }

    @Test
    public void testExecuteUpdateWithColumnIndexes() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";
        final int[] columnIndexes = {1, 2, 3};

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, columnIndexes)).thenReturn(100);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        int result = statment.executeUpdate(query, columnIndexes);

        assertThat(result, is(100));
        verify(stat).executeUpdate(query, columnIndexes);
        verifyListener(listener, "executeUpdate", query, query, columnIndexes);
    }

    @Test
    public void testExecuteUpdateWithColumnIndexesForException() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";
        final int[] columnIndexes = {1, 2, 3};

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, columnIndexes)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.executeUpdate(query, columnIndexes);
            fail();
        } catch (SQLException e) {

        }

        verify(stat).executeUpdate(query, columnIndexes);
        verifyListenerForException(listener, "executeUpdate", query, query, columnIndexes);
    }

    @Test
    public void testExecuteUpdateWithColumnNames() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";
        final String[] columnNames = {"foo", "bar", "baz"};

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, columnNames)).thenReturn(100);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        int result = statment.executeUpdate(query, columnNames);

        assertThat(result, is(100));
        verify(stat).executeUpdate(query, columnNames);
        verifyListener(listener, "executeUpdate", query, query, columnNames);
    }

    @Test
    public void testExecuteUpdateWithColumnNamesForException() throws Exception {
        final String query = "insert into emp (id, name) values (1, 'foo')";
        final String[] columnNames = {"foo", "bar", "baz"};

        Statement stat = mock(Statement.class);
        when(stat.executeUpdate(query, columnNames)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.executeUpdate(query, columnNames);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).executeUpdate(query, columnNames);
        verifyListenerForException(listener, "executeUpdate", query, query, columnNames);
    }


    @Test
    public void testExecute() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        when(stat.execute(query)).thenReturn(true);
        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        boolean result = statment.execute(query);

        assertTrue(result);
        verify(stat).execute(query);
        verifyListener(listener, "execute", query, query);
    }

    @Test
    public void testExecuteForException() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        when(stat.execute(query)).thenThrow(new SQLException());
        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.execute(query);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).execute(query);
        verifyListenerForException(listener, "execute", query, query);
    }

    @Test
    public void testExecuteWithAutoGeneratedKeys() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        when(stat.execute(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(true);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        boolean result = statment.execute(query, Statement.RETURN_GENERATED_KEYS);

        assertTrue(result);
        verify(stat).execute(query, Statement.RETURN_GENERATED_KEYS);
        verifyListener(listener, "execute", query, query, Statement.RETURN_GENERATED_KEYS);
    }

    @Test
    public void testExecuteWithAutoGeneratedKeysForException() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        when(stat.execute(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.execute(query, Statement.RETURN_GENERATED_KEYS);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).execute(query, Statement.RETURN_GENERATED_KEYS);
        verifyListenerForException(listener, "execute", query, query, Statement.RETURN_GENERATED_KEYS);
    }

    @Test
    public void testExecuteWithColumnIndexes() throws Exception {
        final String query = "select * from emp";
        final int[] columnIndexes = {1, 2, 3};

        Statement stat = mock(Statement.class);
        when(stat.execute(query, columnIndexes)).thenReturn(true);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        boolean result = statment.execute(query, columnIndexes);

        assertTrue(result);
        verify(stat).execute(query, columnIndexes);
        verifyListener(listener, "execute", query, query, columnIndexes);
    }

    @Test
    public void testExecuteWithColumnIndexesForException() throws Exception {
        final String query = "select * from emp";
        final int[] columnIndexes = {1, 2, 3};

        Statement stat = mock(Statement.class);
        when(stat.execute(query, columnIndexes)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.execute(query, columnIndexes);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).execute(query, columnIndexes);
        verifyListenerForException(listener, "execute", query, query, columnIndexes);
    }

    @Test
    public void testExecuteWithColumnNames() throws Exception {
        final String query = "select * from emp";
        final String[] columnNames = {"foo", "bar", "baz"};

        Statement stat = mock(Statement.class);
        when(stat.execute(query, columnNames)).thenReturn(true);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        boolean result = statment.execute(query, columnNames);

        assertTrue(result);
        verify(stat).execute(query, columnNames);
        verifyListener(listener, "execute", query, query, columnNames);

    }

    @Test
    public void testExecuteWithColumnNamesForException() throws Exception {
        final String query = "select * from emp";
        final String[] columnNames = {"foo", "bar", "baz"};

        Statement stat = mock(Statement.class);
        when(stat.execute(query, columnNames)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.execute(query, columnNames);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).execute(query, columnNames);
        verifyListenerForException(listener, "execute", query, query, columnNames);

    }

    @Test
    public void testExecuteQuery() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        when(stat.executeQuery(query)).thenReturn(rs);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        ResultSet result = statment.executeQuery(query);

        assertThat(result, is(rs));
        verify(stat).executeQuery(query);
        verifyListener(listener, "executeQuery", query, query);
    }

    @Test
    public void testExecuteQueryWithException() throws Exception {
        final String query = "select * from emp";

        Statement stat = mock(Statement.class);
        when(stat.executeQuery(query)).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        try {
            statment.executeQuery(query);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).executeQuery(query);
        verifyListenerForException(listener, "executeQuery", query, query);
    }

    private Statement getProxyStatement(Statement statement, QueryExecutionListener listener) {
        return new JdkJdbcProxyFactory().createStatement(statement, listener, DS_NAME);
    }

    @SuppressWarnings("unchecked")
    private void verifyListener(QueryExecutionListener listener, String methodName, String query, Object... methodArgs) {

        ArgumentCaptor<ExecutionInfo> executionInfoCaptor = ArgumentCaptor.forClass(ExecutionInfo.class);
        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);


        verify(listener).afterQuery(executionInfoCaptor.capture(), queryInfoListCaptor.capture());

        ExecutionInfo execInfo = executionInfoCaptor.getValue();
        assertThat(execInfo.getMethod(), is(notNullValue()));
        assertThat(execInfo.getMethod().getName(), is(methodName));

        assertThat(execInfo.getMethodArgs(), arrayWithSize(methodArgs.length));
        assertThat(execInfo.getMethodArgs(), arrayContaining(methodArgs));
        assertThat(execInfo.getDataSourceName(), is(DS_NAME));
        assertThat(execInfo.getThrowable(), is(nullValue()));

        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();
        assertThat(queryInfoList.size(), is(1));
        QueryInfo queryInfo = queryInfoList.get(0);
        assertThat(queryInfo.getQuery(), is(equalTo(query)));
    }

    @SuppressWarnings("unchecked")
    private void verifyListenerForException(QueryExecutionListener listener, String methodName,
                                            String query, Object... methodArgs) {

        ArgumentCaptor<ExecutionInfo> executionInfoCaptor = ArgumentCaptor.forClass(ExecutionInfo.class);
        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);

        verify(listener).afterQuery(executionInfoCaptor.capture(), queryInfoListCaptor.capture());

        ExecutionInfo execInfo = executionInfoCaptor.getValue();
        assertThat(execInfo.getMethod(), is(notNullValue()));
        assertThat(execInfo.getMethod().getName(), is(methodName));

        assertThat(execInfo.getMethodArgs(), arrayWithSize(methodArgs.length));
        assertThat(execInfo.getMethodArgs(), arrayContaining(methodArgs));
        assertThat(execInfo.getDataSourceName(), is(DS_NAME));
        assertThat(execInfo.getThrowable(), is(instanceOf(SQLException.class)));

        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();
        assertThat(queryInfoList.size(), is(1));
        QueryInfo queryInfo = queryInfoList.get(0);
        assertThat(queryInfo.getQuery(), is(equalTo(query)));
    }


    @Test
    public void testAddBatchException() throws Exception {
        final String queryA = "insert into emp (id, name) values (1, 'foo')";
        final String queryB = "insert into emp (id, name) values (2, 'bar')";

        Statement stat = mock(Statement.class);
        doThrow(new SQLException()).when(stat).addBatch(queryB);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        statment.addBatch(queryA);

        try {
            statment.addBatch(queryB);
            fail();
        } catch (SQLException e) {
        }

        verify(stat).addBatch(queryA);

    }

    @Test
    public void testExecuteBatch() throws Exception {
        final String queryA = "insert into emp (id, name) values (1, 'foo')";
        final String queryB = "insert into emp (id, name) values (2, 'bar')";
        final String queryC = "insert into emp (id, name) values (3, 'baz')";

        Statement stat = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        when(stat.executeQuery(queryA)).thenReturn(rs);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        // run
        statment.addBatch(queryA);
        statment.addBatch(queryB);
        statment.addBatch(queryC);
        int[] result = statment.executeBatch();

        assertThat(result, is(nullValue()));
        verify(stat).addBatch(queryA);
        verify(stat).addBatch(queryB);
        verify(stat).addBatch(queryC);
        verify(stat).executeBatch();
        verifyListenerForExecuteBatch(listener, queryA, queryB, queryC);

    }

    @Test
    public void testExecuteBatchForException() throws Exception {
        final String queryA = "insert into emp (id, name) values (1, 'foo')";
        final String queryB = "insert into emp (id, name) values (2, 'bar')";
        final String queryC = "insert into emp (id, name) values (3, 'baz')";

        Statement stat = mock(Statement.class);
        when(stat.executeBatch()).thenThrow(new SQLException());

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        // run
        statment.addBatch(queryA);
        statment.addBatch(queryB);
        statment.addBatch(queryC);

        try {
            statment.executeBatch();
            fail();
        } catch (SQLException e) {
        }

        verify(stat).addBatch(queryA);
        verify(stat).addBatch(queryB);
        verify(stat).addBatch(queryC);
        verify(stat).executeBatch();
        verifyListenerForExecuteBatchForException(listener, queryA, queryB, queryC);

    }

    @Test
    public void testExecuteBatchWithClearBatch() throws Exception {
        final String queryA = "insert into emp (id, name) values (1, 'foo')";
        final String queryB = "insert into emp (id, name) values (2, 'bar')";
        final String queryC = "insert into emp (id, name) values (3, 'baz')";

        Statement stat = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        when(stat.executeQuery(queryA)).thenReturn(rs);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        Statement statment = getProxyStatement(stat, listener);

        // run
        statment.addBatch(queryA);
        statment.clearBatch();
        statment.addBatch(queryB);
        statment.addBatch(queryC);
        int[] result = statment.executeBatch();

        assertThat(result, is(nullValue()));
        verify(stat).addBatch(queryA);
        verify(stat).clearBatch();
        verify(stat).addBatch(queryB);
        verify(stat).addBatch(queryC);
        verify(stat).executeBatch();
        verifyListenerForExecuteBatch(listener, queryB, queryC);

    }

    @SuppressWarnings("unchecked")
    private void verifyListenerForExecuteBatch(QueryExecutionListener listener, String... queries) {
        ArgumentCaptor<ExecutionInfo> executionInfoCaptor = ArgumentCaptor.forClass(ExecutionInfo.class);
        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);

        verify(listener).afterQuery(executionInfoCaptor.capture(), queryInfoListCaptor.capture());

        ExecutionInfo execInfo = executionInfoCaptor.getValue();
        assertThat(execInfo.getMethod(), is(notNullValue()));
        assertThat(execInfo.getMethod().getName(), is("executeBatch"));
        assertThat(execInfo.getDataSourceName(), is(DS_NAME));
        assertThat(execInfo.getMethodArgs(), is(nullValue()));

        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();

        assertThat(queryInfoList, is(notNullValue()));
        assertThat(queryInfoList.size(), is(queries.length));

        for (int i = 0; i < queries.length; i++) {
            String expectedQuery = queries[i];
            QueryInfo queryInfo = queryInfoList.get(i);
            assertThat(queryInfo.getQuery(), is(expectedQuery));
            assertThat(queryInfo.getQueryArgs(), is(notNullValue()));
            assertThat(queryInfo.getQueryArgs().size(), is(0));
        }
    }

    @SuppressWarnings("unchecked")
    private void verifyListenerForExecuteBatchForException(QueryExecutionListener listener, String... queries) {
        ArgumentCaptor<ExecutionInfo> executionInfoCaptor = ArgumentCaptor.forClass(ExecutionInfo.class);
        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);

        verify(listener).afterQuery(executionInfoCaptor.capture(), queryInfoListCaptor.capture());

        ExecutionInfo execInfo = executionInfoCaptor.getValue();
        assertThat(execInfo.getMethod(), is(notNullValue()));
        assertThat(execInfo.getMethod().getName(), is("executeBatch"));
        assertThat(execInfo.getDataSourceName(), is(DS_NAME));
        assertThat(execInfo.getMethodArgs(), is(nullValue()));
        assertThat(execInfo.getThrowable(), is(instanceOf(SQLException.class)));


        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();

        assertThat(queryInfoList, is(notNullValue()));
        assertThat(queryInfoList.size(), is(queries.length));

        for (int i = 0; i < queries.length; i++) {
            String expectedQuery = queries[i];
            QueryInfo queryInfo = queryInfoList.get(i);
            assertThat(queryInfo.getQuery(), is(expectedQuery));
            assertThat(queryInfo.getQueryArgs(), is(notNullValue()));
            assertThat(queryInfo.getQueryArgs().size(), is(0));
        }

    }

    @Test
    public void testGetTarget() {
        Statement orig = mock(Statement.class);
        Statement proxy = getProxyStatement(orig, null);

        assertThat(proxy, is(not(sameInstance(orig))));
        assertThat(proxy, is(instanceOf(ProxyJdbcObject.class)));

        Object result = ((ProxyJdbcObject) proxy).getTarget();

        assertThat(result, is(instanceOf(Statement.class)));

        Statement resultStmt = (Statement) result;

        assertThat(resultStmt, is(sameInstance(orig)));
    }

    @Test
    public void testUnwrap() throws Exception {
        Statement mock = mock(Statement.class);
        when(mock.unwrap(String.class)).thenReturn("called");

        Statement stat = getProxyStatement(mock, null);

        String result = stat.unwrap(String.class);

        verify(mock).unwrap(String.class);
        assertThat(result, is("called"));
    }

    @Test
    public void testIsWrapperFor() throws Exception {
        Statement mock = mock(Statement.class);
        when(mock.isWrapperFor(String.class)).thenReturn(true);

        Statement stat = getProxyStatement(mock, null);

        boolean result = stat.isWrapperFor(String.class);

        verify(mock).isWrapperFor(String.class);
        assertThat(result, is(true));
    }

    @Test
    public void testGetConnection() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stat = mock(Statement.class);

        when(stat.getConnection()).thenReturn(conn);
        Statement statement = getProxyStatement(stat, null);

        Connection result = statement.getConnection();

        verify(stat).getConnection();

        assertTrue(Proxy.isProxyClass(result.getClass()));

        InvocationHandler handler = Proxy.getInvocationHandler(result);
        assertThat(handler, is(instanceOf(ConnectionInvocationHandler.class)));

        assertThat(result, is(instanceOf(ProxyJdbcObject.class)));
        Object obj = ((ProxyJdbcObject) result).getTarget();

        assertThat(obj, is(instanceOf(Connection.class)));
        Connection resultConn = (Connection) obj;
        assertThat(resultConn, is(sameInstance(conn)));

    }

}
