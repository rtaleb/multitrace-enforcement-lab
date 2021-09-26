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
package enforcementlab;

import static enforcementlab.TraceProvider.SE_ABC;
import static enforcementlab.TraceProvider.SE_CASINO_RANDOM;
import static enforcementlab.GateExperiment.EVENT_SOURCE;
import static enforcementlab.GateExperiment.INTERVAL;
import static enforcementlab.GateExperiment.POLICY;
import static enforcementlab.GateExperiment.PROXY;
import static enforcementlab.GateExperiment.SCORING_FORMULA;
import static enforcementlab.GateExperiment.TIME;
import static enforcementlab.ScoringProcessorProvider.SC_MAXIMIZE_BETS;
import static enforcementlab.ScoringProcessorProvider.SC_MAXIMIZE_GAINS;
import static enforcementlab.ScoringProcessorProvider.SC_MINIMIZE_CHANGES;

import ca.uqac.lif.cep.enforcement.proxy.DeleteAny;
import ca.uqac.lif.cep.enforcement.proxy.InsertAny;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;
import ca.uqac.lif.mtnp.table.ExpandAsColumns;
import ca.uqac.lif.mtnp.table.TransformedTable;
import ca.uqac.lif.synthia.random.RandomBoolean;
import ca.uqac.lif.synthia.random.RandomFloat;
import enforcementlab.abc.Property1;

public class MainLab extends Laboratory
{

	@Override
	public void setup()
	{
		setTitle("A benchmark for multi-trace enforcement pipelines");
		setAuthor("Rania Taleb, Sylvain Hallé and Raphaël Khoury");
		
		// Some random generators
		RandomBoolean r_bool = new RandomBoolean();
		r_bool.setSeed(getRandomSeed());
		RandomFloat r_float = new RandomFloat();
		r_float.setSeed(getRandomSeed());
		
		// The factory that generates the experiments
		TraceProvider p_trace = new TraceProvider(r_bool, r_float);
		ScoringProcessorProvider p_score = new ScoringProcessorProvider();
		ProxyProvider p_proxy = new ProxyProvider(p_trace);
		PolicyProvider p_policy = new PolicyProvider();
		GateExperimentFactory factory = new GateExperimentFactory(this, p_policy, p_proxy, p_trace, p_score);
		
		// A big region with the lab's parameters
		Region big_r = new Region();
		{
			big_r.add(EVENT_SOURCE, SE_ABC);
			big_r.add(POLICY, Property1.NAME);
			big_r.add(PROXY, InsertAny.NAME, DeleteAny.NAME);
			big_r.add(SCORING_FORMULA, SC_MINIMIZE_CHANGES);
			big_r.add(INTERVAL, 1, 3, 5, 7);
			for (Region in_r : big_r.all(EVENT_SOURCE, POLICY, PROXY, SCORING_FORMULA, INTERVAL))
			{
				GateExperiment exp = factory.get(in_r);
				if (exp == null)
				{
					continue;
				}
				ExperimentTable et_events = new ExperimentTable(GateExperiment.INPUT_EVENTS, GateExperiment.OUTPUT_EVENTS);
				et_events.add(exp);
				add(et_events);
				Scatterplot p_events = new Scatterplot(et_events);
				add(p_events);
			}
		}
		
		// Comparing the impact of interval length on time for brute-force vs. prefix tree
		/*{
			Region r = new Region();
			r.add(EVENT_SOURCE, SE_CASINO_RANDOM);
			r.add(SCORING_FORMULA, SC_MAXIMIZE_BETS, SC_MAXIMIZE_GAINS);
			r.add(INTERVAL, 1, 100, 500, 2000);
			r.add(TECHNIQUE, T_BRUTE_FORCE, T_PREFIX_TREE);
			for (Region r_p : r.all(SCORING_FORMULA, EVENT_SOURCE))
			{
				String formula = r_p.getString(SCORING_FORMULA);
				ExperimentTable et_time = new ExperimentTable(INTERVAL, TECHNIQUE, TIME);
				et_time.setShowInList(false);
				TransformedTable tt_time = new TransformedTable(new ExpandAsColumns(TECHNIQUE, TIME), et_time);
				tt_time.setNickname("tTimeBrutePrefixInterval" + LatexNamer.latexify(formula));
				tt_time.setTitle("Impact of interval on running time, brute force vs. prefix tree, scoring formula " + formula);
				add(et_time, tt_time);
				for (Region r_i : r_p.all(INTERVAL, TECHNIQUE))
				{
					MultiTraceSelectorExperiment e = factory.get(r_i);
					if (r_i == null)
					{
						continue;
					}
					et_time.add(e);
				}
			}
		}*/
	}
	
	public static void main(String[] args)
	{
		MainLab.initialize(args, MainLab.class);
	}

}