package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;
import net.ttddyy.dsproxy.proxy.PreparedStatementInvocationHandler;
import net.ttddyy.dsproxy.proxy.ProxyJdbcObject;
import net.ttddyy.dsproxy.proxy.StatementInvocationHandler;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Tadaya Tsuyukubo
 */
public class ConnectionInvocationHandlerMockTest {

    @Test
    public void testCreateStatementWithNoParam() throws Throwable {
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        Statement statement = connection.createStatement();

        verifyStatement(statement);
        verify(conn).createStatement();
    }

    @Test
    public void testCreateStatementWithTwoParam() throws Throwable {
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);


        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        verifyStatement(statement);
        verify(conn).createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Test
    public void testCreateStatementWithThreeParam() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        // run
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

        // verify
        verifyStatement(statement);
        verify(conn).createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    private Connection getProxyConnection(Connection mockConnection) {
        QueryExecutionListener listener = mock(QueryExecutionListener.class);
        return new JdkJdbcProxyFactory().createConnection(mockConnection, listener, "myDS");
    }

    private void verifyStatement(Statement statement) {
        assertThat(statement, notNullValue());

        assertTrue(Proxy.isProxyClass(statement.getClass()));
        InvocationHandler handler = Proxy.getInvocationHandler(statement);
        assertThat(handler, is(instanceOf(StatementInvocationHandler.class)));
    }


    @Test
    public void testPrepareStatement() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";

        // run
        PreparedStatement statement = connection.prepareStatement(query);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query);
    }

    @Test
    public void testPrepareStatementWithAutoGeneratedKeys() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";

        // run
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    @Test
    public void testPrepareStatementWithColumnIndexes() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";
        int[] columnIndexes = new int[]{1, 2, 3};

        // run
        PreparedStatement statement = connection.prepareStatement(query, columnIndexes);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query, columnIndexes);
    }

    @Test
    public void testPrepareStatementWithColumnNames() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";
        String[] columnNames = new String[]{"id", "name"};

        // run
        PreparedStatement statement = connection.prepareStatement(query, columnNames);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query, columnNames);
    }

    @Test
    public void testPrepareStatementWithTwoResultSetParams() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";

        // run
        PreparedStatement statement = connection.prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Test
    public void testPrepareStatementWithThreeResultSetParams() throws Throwable {
        // expect
        Connection conn = mock(Connection.class);
        Connection connection = getProxyConnection(conn);

        String query = "select * from emp";

        // run
        PreparedStatement statement = connection.prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

        // verify
        verifyPreparedStatement(statement);
        verify(conn).prepareStatement(query,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    private void verifyPreparedStatement(PreparedStatement statement) {
        assertThat(statement, notNullValue());

        assertTrue(Proxy.isProxyClass(statement.getClass()));
        InvocationHandler handler = Proxy.getInvocationHandler(statement);
        assertThat(handler, is(instanceOf(PreparedStatementInvocationHandler.class)));
    }


    @Test
    public void testGetTarget() {
        Connection orig = mock(Connection.class);
        Connection proxy = getProxyConnection(orig);

        assertThat(proxy, is(not(sameInstance(orig))));
        assertThat(proxy, is(instanceOf(ProxyJdbcObject.class)));

        Object result = ((ProxyJdbcObject) proxy).getTarget();

        assertThat(result, is(instanceOf(Connection.class)));

        Connection resultConn = (Connection) result;

        assertThat(resultConn, is(sameInstance(orig)));
    }

        @Test
    public void testUnwrap() throws Exception {
        Connection mock = mock(Connection.class);
        when(mock.unwrap(String.class)).thenReturn("called");

        Connection conn = getProxyConnection(mock);

        String result = conn.unwrap(String.class);

        verify(mock).unwrap(String.class);
        assertThat(result, is("called"));
    }

    @Test
    public void testIsWrapperFor() throws Exception {
        Connection mock = mock(Connection.class);
        when(mock.isWrapperFor(String.class)).thenReturn(true);

        Connection conn = getProxyConnection(mock);

        boolean result = conn.isWrapperFor(String.class);

        verify(mock).isWrapperFor(String.class);
        assertThat(result, is(true));
    }
}
