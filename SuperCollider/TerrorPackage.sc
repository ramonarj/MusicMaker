
TerrorPackage : Package {

	// Variables de la clase
	var <tempo = 3.0; // 40bpm -> 40/60bps -> 60/40 -> 60/40*2 = 3.0

		init {

		arg parameters;

		percs = Dictionary.new;
		chords = Dictionary.new;
		melodies = Dictionary.new;
		oneShots = Dictionary.new;

		params = MusicMakerParameters.new;
		params.init(1, 0);

		params.basetempo = tempo;
		params.actualTempo = tempo;

		percs.add(\BasePercs -> {
			Pdef(
				\rhythm,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/2], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_kicks_high], 1),
							Prand(~buff[\percs_kicks_low], 1),
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.5, 0.7, 7)], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		percs.add(\ReBasePercs -> {
			Pdef(
				\rhythm,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/2], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_kicks_high], 1),
							Prand(~buff[\percs_kicks_low], 1),
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.5, 0.7, 7)], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		percs.add(\StopBasePercs -> {
			Pdef(\rhythm).stop;
		});

		percs.add(\ExtraPercs -> {
			Pdef(
				\rhythm2,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/4], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][1],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
						], inf
					),
					\amp, 2,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		percs.add(\ReExtraPercs -> {
			Pdef(
				\rhythm2,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/4], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][1],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
						], inf
					),
					\amp, 2,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		percs.add(\StopExtraPercs -> {
			Pdef(\rhythm2).stop;
		});

		percs.add(\EffectsPercs -> {
			Pdef(
				\rhythm3,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/4], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_bigbells][2],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
						], inf
					),
					\amp, 2.75,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		percs.add(\ReEffectsPercs -> {
			Pdef(
				\rhythm3,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/4], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_bigbells][2],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
							~buff[\percs_bigbells][0],
						], inf
					),
					\amp, 2.75,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		percs.add(\StopEffectsPercs -> {
			Pdef(\rhythm3).stop;
		});


		chords.add(\BaseHarmony -> {
			~creepyHarmony = Pbind(
				\instrument, \bpfsaw,
				\dur, Pwhite(4.5, 5.0, inf),
				\midinote, Pxrand(~autoChords, inf),
				\detune,1,
				\cfmin, 100,
				\cfmax, 200,
				\rqmin, Pexprand(0.05, 0.05, inf),
				\atk, Pwhite(2.0, 2.5, inf),
				\rel, Pwhite(5, 6.0, inf),
				\ldb, 6,
				\amp, 2,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play(quant:params.actualTempo);
		});

		chords.add(\ReBaseHarmony -> {
			// LA ARMONIA DE ESTE PAQUETE NO SIGUE PATRONES RITMICOS NI SE AJUSTA A UN TEMPO
			// ES PSEUDOALEATORIO CADA CAMBIO
		});

		chords.add(\StopBaseHarmony -> {
			~creepyHarmony.stop;
		});

		chords.add(\ExtraHarmony -> {
			~softChords = Pbind(
				\instrument, \organDonor,
				\dur, Pwhite(4.5, 5.0, inf),
				\midinote, Pxrand(~autoChords, inf),
				\rqmin, Pexprand(0.05, 0.05, inf),
				\att, 3.5,
				\rel, Pwhite(5, 6.0, inf),
				\ldb, 1,
				\amp, 1,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
				\cutoff, 100,
			).play(quant:params.actualTempo);
		});

		chords.add(\ReExtraHarmony -> {
			// LA ARMONIA DE ESTE PAQUETE NO SIGUE PATRONES RITMICOS NI SE AJUSTA A UN TEMPO
			// ES PSEUDOALEATORIO CADA CAMBIO
		});

		chords.add(\StopExtraHarmony -> {
			~softChords.stop;
		});


		melodies.add(\FirstMelody -> {
			Pdef(
				\melodies,
				Pbind(
					\instrument, \bpfsaw,
					\dur, Pwhite(2.5,4.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[74],[75],[77],[79],[80],[82]], 1),
							Pseq([[75],[74],[72],[82],[80],[79],[82]], 1),
							Pseq([[72],[75], [72]], 1),
							Pseq([[82],[79], [82]], 1),
						],
						inf
					),
					\detune, Pexprand(0.25,0.5),
					\cfmin, 100,
					\cfmax, 1000,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(1.25,1.75),
					\rel, Pwhite(7.5,10.5),
					\ldb, 6,
					\amp, 0.65,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReFirstMelody -> {
			// LA MELODIA DE ESTE PAQUETE NO SIGUE PATRONES RITMICOS NI SE AJUSTA A UN TEMPO
			// ES PSEUDOALEATORIO CADA CAMBIO
		});

		melodies.add(\StopFirstMelody -> {
			Pdef(\melodies).stop;
		});

		melodies.add(\SecondMelody -> {
			Pdef(
				\melodies,
				Pbind(
					\instrument, \organDonor,
					\dur, Pwhite(1.5,3.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[74],[75],[77],[79],[80],[82]], 1),
							Pseq([[75],[74],[72],[82],[80],[79],[82]], 1),
							Pseq([[72],[75], [72]], 1),
							Pseq([[82],[79], [82]], 1),
						],
						inf
					),
					\att, Pwhite(4.25,5.5),
					\rel, Pwhite(5.5,7.5),
					\amp, 4.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReSecondMelody -> {
			// LA MELODIA DE ESTE PAQUETE NO SIGUE PATRONES RITMICOS NI SE AJUSTA A UN TEMPO
			// ES PSEUDOALEATORIO CADA CAMBIO
		});

		melodies.add(\StopSecondMelody -> {
			Pdef(\melodies2).stop;
		});

		melodies.add(\ThirdMelody -> {
			Pdef(
				\melodies,
				Pbind(
					\instrument, \organDonor,
					\dur, Pwhite(5.0,8.0),
					\midinote, Pxrand
					(
						[
							[84],[86],[87],[89],[91],[92],[94],
						],
						inf
					),
					\att, Pwhite(1.25,2.5),
					\rel, Pwhite(6.5,8.5),
					\amp, 5.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReThirdMelody -> {
			// LA MELODIA DE ESTE PAQUETE NO SIGUE PATRONES RITMICOS NI SE AJUSTA A UN TEMPO
			// ES PSEUDOALEATORIO CADA CAMBIO
		});

		melodies.add(\StopThirdMelody -> {
			Pdef(\melodies3).stop;
		});


		oneShots.add(\FirstOS -> {
			12.do{
				Synth(
					\bpfsaw,
					[
						\atk, exprand(0.5,1.5),
						\rel, exprand(2.0,8.0),
						\c1, exprand(4,10.0),
						\c2, exprand(2.0,5).neg,
						\freq, exprand(8,60),
						\detune, rrand(0.1,4),
						\cfmin, 30,
						\cfmax, 400,
						\rqmin, 0.02,
						\rqmax, 0.08,
						\amp, exprand(4.5,6.9),
						\pan, rrand(-0.5,0.5),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});

		oneShots.add(\SecondOS -> {
			1.do{
				Synth(
					\bpfbuf,
					[
						\buf, ~buff[\sfx_terror_strings][0].bufnum,
						\amp, exprand(5.5,8.5),
						\pan, rrand(-0.9,0.9),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});

		oneShots.add(\ThirdOS -> {
			1.do{
				Synth(
					\bpfbuf,
					[
						\buf, ~buff[\sfx_terror_strings][1].bufnum,
						\amp, exprand(3.5,6.5),
						\pan, rrand(-0.9,0.9),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});
	}
}