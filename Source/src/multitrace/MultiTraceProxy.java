/*
    A benchmark for multi-trace runtime enforcement in BeepBeep 3
    Copyright (C) 2021 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package multitrace;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.Processor;

/**
 * Takes as input a trace of events, and produces a sequence of
 * multi-trace elements.
 */
public class MultiTraceProxy extends GroupProcessor
{
	/**
	 * A processor turning multi-events into multi-trace elements.
	 */
	protected AppendToMultiTrace m_appender;
	
	/**
	 * Creates a new proxy.
	 * @param proxy The processor producing multi-events
	 */
	public MultiTraceProxy(Processor proxy)
	{
		super(1, 1);
		m_appender = new AppendToMultiTrace();
		Connector.connect(proxy, m_appender);
		addProcessors(proxy, m_appender);
		associateInput(0, proxy, 0);
		associateOutput(0, m_appender, 0);
	}
	
	/**
	 * Tells the processor to restart a new multi-event tree.
	 */
	public void restartTree()
	{
		m_appender.reset();
	}
}
