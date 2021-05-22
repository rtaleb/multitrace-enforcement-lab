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
package multitrace.lab.casino;

import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.Troolean.Value;

public class CasinoFunction
{
	public static final IsBet isBet = new IsBet();
	
	public static final IsStartGame isStart = new IsStartGame();
	
	public static final IsEndGame isEnd = new IsEndGame();
	
	public static final CasinoPays casinoPays = new CasinoPays();
	
	public static final CasinoPaid casinoPaid = new CasinoPaid();

	public static class IsBet extends UnaryFunction<CasinoEvent,Troolean.Value>
	{
		protected IsBet()
		{
			super(CasinoEvent.class, Troolean.Value.class);
		}

		@Override
		public Value getValue(CasinoEvent e)
		{
			if (e instanceof CasinoEvent.Bet)
			{
				return Troolean.Value.TRUE;
			}
			return Troolean.Value.FALSE;
		}

		@Override
		public String toString()
		{
			return "Bet(.)";
		}
	}

	public static class IsStartGame extends UnaryFunction<CasinoEvent,Troolean.Value>
	{
		protected IsStartGame()
		{
			super(CasinoEvent.class, Troolean.Value.class);
		}

		@Override
		public Value getValue(CasinoEvent e)
		{
			if (e instanceof CasinoEvent.StartGame)
			{
				return Troolean.Value.TRUE;
			}
			return Troolean.Value.FALSE;
		}

		@Override
		public String toString()
		{
			return "StartGame(.)";
		}
	}

	public static class IsEndGame extends UnaryFunction<CasinoEvent,Troolean.Value>
	{
		protected IsEndGame()
		{
			super(CasinoEvent.class, Troolean.Value.class);
		}

		@Override
		public Value getValue(CasinoEvent e)
		{
			if (e instanceof CasinoEvent.EndGame)
			{
				return Troolean.Value.TRUE;
			}
			return Troolean.Value.FALSE;
		}

		@Override
		public String toString()
		{
			return "EndGame(.)";
		}
	}

	public static class CasinoPays extends UnaryFunction<CasinoEvent,Troolean.Value>
	{
		protected CasinoPays()
		{
			super(CasinoEvent.class, Troolean.Value.class);
		}

		@Override
		public Value getValue(CasinoEvent e)
		{
			if (e instanceof CasinoEvent.Pay && ((CasinoEvent.Pay) e).getFrom().compareTo("casino") == 0)
			{
				return Troolean.Value.TRUE;
			}
			return Troolean.Value.FALSE;
		}

		@Override
		public String toString()
		{
			return "Pay(casino,.)";
		}
	}
	
	public static class CasinoPaid extends UnaryFunction<CasinoEvent,Troolean.Value>
	{
		protected CasinoPaid()
		{
			super(CasinoEvent.class, Troolean.Value.class);
		}

		@Override
		public Value getValue(CasinoEvent e)
		{
			if (e instanceof CasinoEvent.Pay && ((CasinoEvent.Pay) e).getTo().compareTo("casino") == 0)
			{
				return Troolean.Value.TRUE;
			}
			return Troolean.Value.FALSE;
		}

		@Override
		public String toString()
		{
			return "Pay(.,casino)";
		}
	}
}
