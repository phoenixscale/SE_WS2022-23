package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import cpath.Project;
import cpath.Workpackage;

/*
 * @author Franz Hackenberg, 		MN:3338863, st155960@stud.uni-stuttgart.de
 * @author Clemens Korneli, 		MN:3538867, st172943@stud.uni-stuttgart.de
 * @author Jannick Leif Samietz, 	MN:3333295, st158516@stud.uni-stuttgart.de
 * @author Christian Staib, 		MN:3391431, st162081@stud.uni-stuttgart.de
 * @author Florian Wuest, 			MN:3528877, st171957@stud.uni-stuttgart.de
 */

/**
 * Testet die Implementierung mit dem Beispiel aus der Vorlesung.
 */
public class ExerciseExampleTest {
	private static Workpackage a = new Workpackage("A01", 3);
	private static Workpackage b = new Workpackage("A02", 4);
	private static Workpackage c = new Workpackage("A03", 5);
	private static Workpackage d = new Workpackage("A04", 4);
	private static Workpackage e = new Workpackage("A05", 9);
	private static Workpackage f = new Workpackage("A06", 4);
	private static Workpackage g = new Workpackage("A07", 2);
	private static Workpackage h = new Workpackage("A08", 4);
	private static Workpackage i = new Workpackage("A09", 2);
	private static Workpackage j = new Workpackage("A10", 3);
	private static Workpackage k = new Workpackage("A11", 3);
	private static Workpackage l = new Workpackage("A12", 2);
	private static Workpackage m = new Workpackage("A13", 4);
	private static Workpackage n = new Workpackage("A14", 7);
	private static Workpackage o = new Workpackage("A15", 2);
	private static Workpackage p = new Workpackage("A16", 4);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Project graph = new Project();

		graph.getStartNodes().add(a);
		graph.getStartNodes().add(b);
		graph.getStartNodes().add(c);

		a.addSuccessor(d).addSuccessor(h);
		a.addSuccessor(e).addSuccessor(l);
		h.addSuccessor(l).addSuccessor(n);
		l.addSuccessor(o).addSuccessor(p);
		b.addSuccessor(f).addSuccessor(i).addSuccessor(n);
		i.addSuccessor(o);
		f.addSuccessor(j).addSuccessor(m);
		c.addSuccessor(g).addSuccessor(k).addSuccessor(m);
		m.addSuccessor(p);


		graph.computeCriticalPath();
		System.out.println();
	}

	@Test
	public void testNodeA() {
		assertEquals(3, a.getDuration());
		assertEquals(0, a.getEarliestStart());
		assertEquals(3, a.getEarliestFinish());
		assertEquals(0, a.getLatestStart());
		assertEquals(3, a.getLatestFinish());
		assertEquals(0, a.getSlack());
	}

	@Test
	public void testNodeB() {
		assertEquals(4, b.getDuration());
		assertEquals(0, b.getEarliestStart());
		assertEquals(4, b.getEarliestFinish());
		assertEquals(2, b.getLatestStart());
		assertEquals(6, b.getLatestFinish());
		assertEquals(2, b.getSlack());
	}

	@Test
	public void testNodeC() {
		assertEquals(5, c.getDuration());
		assertEquals(0, c.getEarliestStart());
		assertEquals(5, c.getEarliestFinish());
		assertEquals(3, c.getLatestStart());
		assertEquals(8, c.getLatestFinish());
		assertEquals(3, c.getSlack());
	}

	@Test
	public void testNodeD() {
		assertEquals(4, d.getDuration());
		assertEquals(3, d.getEarliestStart());
		assertEquals(7, d.getEarliestFinish());
		assertEquals(4, d.getLatestStart());
		assertEquals(8, d.getLatestFinish());
		assertEquals(1, d.getSlack());
	}

	@Test
	public void testNodeE() {
		assertEquals(9, e.getDuration());
		assertEquals(3, e.getEarliestStart());
		assertEquals(12, e.getEarliestFinish());
		assertEquals(3, e.getLatestStart());
		assertEquals(12, e.getLatestFinish());
		assertEquals(0, e.getSlack());
	}

	@Test
	public void testNodeF() {
		assertEquals(4, f.getDuration());
		assertEquals(4, f.getEarliestStart());
		assertEquals(8, f.getEarliestFinish());
		assertEquals(6, f.getLatestStart());
		assertEquals(10, f.getLatestFinish());
		assertEquals(2, f.getSlack());
	}

	@Test
	public void testNodeG() {
		assertEquals(2, g.getDuration());
		assertEquals(5, g.getEarliestStart());
		assertEquals(7, g.getEarliestFinish());
		assertEquals(8, g.getLatestStart());
		assertEquals(10, g.getLatestFinish());
		assertEquals(3, g.getSlack());
	}

	@Test
	public void testNodeH() {
		assertEquals(4, h.getDuration());
		assertEquals(7, h.getEarliestStart());
		assertEquals(11, h.getEarliestFinish());
		assertEquals(8, h.getLatestStart());
		assertEquals(12, h.getLatestFinish());
		assertEquals(1, h.getSlack());
	}

	@Test
	public void testNodeI() {
		assertEquals(2, i.getDuration());
		assertEquals(8, i.getEarliestStart());
		assertEquals(10, i.getEarliestFinish());
		assertEquals(12, i.getLatestStart());
		assertEquals(14, i.getLatestFinish());
		assertEquals(4, i.getSlack());
	}

	@Test
	public void testNodeJ() {
		assertEquals(3, j.getDuration());
		assertEquals(8, j.getEarliestStart());
		assertEquals(11, j.getEarliestFinish());
		assertEquals(10, j.getLatestStart());
		assertEquals(13, j.getLatestFinish());
		assertEquals(2, j.getSlack());
	}
//--------------------------------------------------
	@Test
	public void testNodeK() {
		assertEquals(3, k.getDuration());
		assertEquals(7, k.getEarliestStart());
		assertEquals(10, k.getEarliestFinish());
		assertEquals(10, k.getLatestStart());
		assertEquals(13, k.getLatestFinish());
		assertEquals(3, k.getSlack());
	}

	@Test
	public void testNodeL() {
		assertEquals(2, l.getDuration());
		assertEquals(12, l.getEarliestStart());
		assertEquals(14, l.getEarliestFinish());
		assertEquals(12, l.getLatestStart());
		assertEquals(14, l.getLatestFinish());
		assertEquals(0, l.getSlack());
	}

	@Test
	public void testNodeM() {
		assertEquals(4, m.getDuration());
		assertEquals(11, m.getEarliestStart());
		assertEquals(15, m.getEarliestFinish());
		assertEquals(13, m.getLatestStart());
		assertEquals(17, m.getLatestFinish());
		assertEquals(2, m.getSlack());
	}

	@Test
	public void testNodeN() {
		assertEquals(7, n.getDuration());
		assertEquals(14, n.getEarliestStart());
		assertEquals(21, n.getEarliestFinish());
		assertEquals(14, n.getLatestStart());
		assertEquals(21, n.getLatestFinish());
		assertEquals(0, n.getSlack());
	}

	@Test
	public void testNodeO() {
		assertEquals(2, o.getDuration());
		assertEquals(14, o.getEarliestStart());
		assertEquals(16, o.getEarliestFinish());
		assertEquals(15, o.getLatestStart());
		assertEquals(17, o.getLatestFinish());
		assertEquals(1, o.getSlack());
	}

	@Test
	public void testNodeP() {
		assertEquals(4, p.getDuration());
		assertEquals(16, p.getEarliestStart());
		assertEquals(20, p.getEarliestFinish());
		assertEquals(17, p.getLatestStart());
		assertEquals(21, p.getLatestFinish());
		assertEquals(1, p.getSlack());
	}


}
