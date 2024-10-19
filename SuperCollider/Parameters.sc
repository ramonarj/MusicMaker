
MusicMakerParameters {

	var
	<>basetempo, <>actualTempo,
	<octave,
	<num_percs_layers = 3, <percs_layers,
	<num_harmonic_layers = 2, <harmonic_layers,
	<num_melodic_layers = 3, <melodic_layers,
	<num_one_shots = 4;

	init{ arg baseT, oct;
		basetempo = baseT;
		actualTempo = baseT;
		octave = oct;

		percs_layers = Array.fill(num_percs_layers, {false;});
		harmonic_layers = Array.fill(num_harmonic_layers, {false;});
		melodic_layers = Array.fill(num_melodic_layers, {false;});
	}


	mulTempo{ arg mult;
		actualTempo = basetempo * (1 / mult);
	}

	setOctave{ arg oct;
		octave = oct;
	}


	activatePercsLayer{ arg layer;
		percs_layers[layer] = true;
	}
	activateHarmonicLayer{ arg layer;
		harmonic_layers[layer] = true;
	}
	activateMelodicLayer{ arg layer;
		melodic_layers[layer] = true;
	}


	deactivatePercsLayer{ arg layer;
		percs_layers[layer] = false;
	}
	deactivateHarmonicLayer{ arg layer;
		harmonic_layers[layer] = false;
	}
	deactivateMelodicLayer{ arg layer;
		melodic_layers[layer] = false;
	}
}
