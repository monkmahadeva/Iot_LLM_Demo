
import org.junit.Assert.assertEquals
import org.junit.Test

class DemoClassTest {
    @Test
    fun testSayHello() {
        val demo = DemoClass()
        assertEquals("Hello, coverage!", demo.sayHello())
    }
}