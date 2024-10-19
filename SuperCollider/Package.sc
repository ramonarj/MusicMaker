
Package {

	var <percs, <chords, <melodies, <oneShots, <params;

	init{}

	playAll{}

	//Pausa todos los elementos (menos los OS)
	stopAll
	{
		//Ritmos
		for(0, 2, {arg i; this.stopRhythm(i.asInteger())});
		//Melodías
		for(0, 1, {arg i; this.stopMelody(i.asInteger())});
		//Armonías
		for(0, 2, {arg i; this.stopHarmony(i.asInteger())});
	}

	//Recibe el id de la capa de percusion que sonara. Si esta no esta sonando la activara y la marcara como sonando.
	//En caso contrario no pasara nada.
	playRhythm{ arg capa;

		switch(capa,
		0, {
			if(params.percs_layers[capa] == false ,{params.activatePercsLayer(capa); percs[\BasePercs].value});
		},
		1, {
			if(params.percs_layers[capa] == false ,{params.activatePercsLayer(capa); percs[\ExtraPercs].value});
		},
		2, {
			if(params.percs_layers[capa] == false ,{params.activatePercsLayer(capa); percs[\EffectsPercs].value});
		}
		)
	}

	//Recibe el id de la capa de percusion que se detendra. Si esta sonando la detendra y la marcara como no sonando.
	//En caso contrario no pasara nada.
	stopRhythm{ arg capa;

		switch(capa,
		0, {
			if(params.percs_layers[capa] == true ,{params.deactivatePercsLayer(capa); percs[\StopBasePercs].value});
		},
		1, {
			if(params.percs_layers[capa] == true ,{params.deactivatePercsLayer(capa); percs[\StopExtraPercs].value});
		},
		2, {
			if(params.percs_layers[capa] == true ,{params.deactivatePercsLayer(capa); percs[\StopEffectsPercs].value});
		}
		)
	}

	//Recibe el id de la capa harmonica que sonara. Si esta no esta sonando la activara y la marcara como sonando.
	//En caso contrario no pasara nada.
	playHarmony{ arg capa;

		switch(capa,
		0, {
			if(params.harmonic_layers[capa] == false ,{params.activateHarmonicLayer(capa); chords[\BaseHarmony].value});
		},
		1, {
			if(params.harmonic_layers[capa] == false ,{params.activateHarmonicLayer(capa); chords[\ExtraHarmony].value});
		}
		)
	}

	//Recibe el id de la capa harmonica que se detendra. Si esta sonando la detendra y la marcara como no sonando.
	//En caso contrario no pasara nada.
	stopHarmony{ arg capa;

		switch(capa,
		0, {
			if(params.harmonic_layers[capa] == true ,{params.deactivateHarmonicLayer(capa); chords[\StopBaseHarmony].value});
		},
		1, {
			if(params.harmonic_layers[capa] == true ,{params.deactivateHarmonicLayer(capa); chords[\StopExtraHarmony].value});
		}
		)
	}

	//Recibe el id de la capa melodica que sonara. Si esta no esta sonando la activara y la marcara como sonando.
	//En caso contrario no pasara nada.
	playMelody{ arg capa;

		switch(capa,
		0, {
			if(params.melodic_layers[capa] == false ,{params.activateMelodicLayer(capa); melodies[\FirstMelody].value});
		},
		1, {
			if(params.melodic_layers[capa] == false ,{params.activateMelodicLayer(capa); melodies[\SecondMelody].value});
		},
		2, {
			if(params.melodic_layers[capa] == false ,{params.activateMelodicLayer(capa); melodies[\ThirdMelody].value});
		}
		)
	}

	//Recibe el id de la capa melodica que se detendra. Si esta sonando la detendra y la marcara como no sonando.
	//En caso contrario no pasara nada.
	stopMelody{ arg capa;

		switch(capa,
		0, {
			if(params.melodic_layers[capa] == true ,{params.deactivateMelodicLayer(capa); melodies[\StopFirstMelody].value});
		},
		1, {
			if(params.melodic_layers[capa] == true ,{params.deactivateMelodicLayer(capa); melodies[\StopSecondMelody].value});
		},
		2, {
			if(params.melodic_layers[capa] == true ,{params.deactivateMelodicLayer(capa); melodies[\StopThirdMelody].value});
		}
		)
	}

	//Recibe el id del one shot que se lanzara, sin tener en cuenta nada mas.
	playOS{ arg capa;

		switch(capa,
		0, {
			oneShots[\FirstOS].value;
		},
		1, {
			oneShots[\SecondOS].value;
		},
		2, {
			oneShots[\ThirdOS].value;
		},
		3, {
			oneShots[\FourthOS].value;
		},
		4, {
			oneShots[\FifthOS].value;
		}
		)
	}

	//Multiplica el tempo de todas las capas que estan sonando.
	multiplyTempo{ arg mult;

		params.mulTempo(mult);

		percs[\ReBasePercs].value;
		percs[\ReExtraPercs].value;
		percs[\ReEffectsPercs].value;

		chords[\ReBaseHarmony].value;
		chords[\ReExtraHarmony].value;

		melodies[\ReFirstMelody].value;
		melodies[\ReSecondMelody].value;
		melodies[\ReThirdMelody].value;
	}
}

// METODOS

// BasePercs, ReBasePercs, StopPercs
// ExtraPercs, ReExtraPercs, StopExtraPercs
// EffectsPercs, ReEffectsPercs, StopEffectsPercs

// BaseHarmony, ReBaseHarmony, StopBaseHarmony
// ExtraHarmony, ReExtraHarmony, StopExtraHarmony

// FirstMelody, ReFirstMelody, StopFirstMelody
// SecondMelody, ReSecondMelody, StopSecondMelody
// ThirdMelody, ReThirdMelody, StopThirdMelody

// FirstOneShot, SecondOneShot, ThirdOneShot, FourthOneShot, FifthOneShot