
FantasyPackage : Package {

	// Variables de la clase
	var <tempo = 1.2857143; // 140bpm -> 140/60bps -> 60/140spb -> 60/140*3spc = 1.2857143

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
					\dur, Pseq([1/6], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_forest_low], 1),
							~buff[\percs_forest_high][0],
							~buff[\percs_forest_high][0],
							~buff[\percs_forest_high][1],
							~buff[\percs_forest_high][1],
							~buff[\percs_forest_high][2],
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
					\dur, Pseq([1/6], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_forest_low], 1),
							~buff[\percs_forest_high][0],
							~buff[\percs_forest_high][0],
							~buff[\percs_forest_high][1],
							~buff[\percs_forest_high][1],
							~buff[\percs_forest_high][2],
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
					\dur, Pseq([1/1], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_forest_esp][0],
							~buff[\percs_forest_esp][1],
						], inf
					),
					\amp, 1,
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
					\dur, Pseq([1/1], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_forest_esp][0],
							~buff[\percs_forest_esp][1],
						], inf
					),
					\amp, 1,
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
					\dur, Pseq([1/6], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_jinglebells][1],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.04, 0.1, 7)], inf),
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
					\dur, Pseq([1/6], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_jinglebells][1],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
							~buff[\percs_jinglebells][0],
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.04, 0.1, 7)], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		percs.add(\StopEffectsPercs -> {
			Pdef(\rhythm3).stop;
		});


		chords.add(\BaseHarmony -> {
			Pdef(
				\chord,
				Pbind(
					\instrument, \bpfsaw,
					\dur, 5.0,
					\midinote, Pxrand([
						[60, 66, 67]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 1000,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(1.5,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.7,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		chords.add(\ReBaseHarmony -> {
			Pdef(
				\chord,
				Pbind(
					\instrument, \bpfsaw,
					\dur, 5.0,
					\midinote, Pxrand([
						[60, 66, 67]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 1000,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(1.5,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.7,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		chords.add(\StopBaseHarmony -> {
			Pdef(\chord).stop;
		});

		chords.add(\ExtraHarmony -> {
			Pdef(
				\chord2,
				Pbind(
					\instrument, \bpfsaw,
					\dur, 4.0,
					\midinote, Pxrand([
						[72, 78]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 800,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(1.5,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		chords.add(\ReExtraHarmony -> {
			Pdef(
				\chord2,
				Pbind(
					\instrument, \bpfsaw,
					\dur, 4.0,
					\midinote, Pxrand([
						[72, 78]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 800,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(1.5,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		chords.add(\StopExtraHarmony -> {
			Pdef(\chord2).stop;
		});


		melodies.add(\FirstMelody -> {
			Pdef(
				\melodies,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/1], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Pxrand(~buff[\scale_lydian], 1),
						], inf
					),
					\rel, Pwhite(8.0),
					\amp, Pseq([2, 3], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReFirstMelody -> {
			Pdef(
				\melodies,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pseq([1/2], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Pxrand(~buff[\scale_lydian], 1),
						], inf
					),
					\rel, Pwhite(8.0),
					\amp, Pseq([2, 3], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		melodies.add(\StopFirstMelody -> {
			Pdef(\melodies).stop;
		});

		melodies.add(\SecondMelody -> {
			Pdef(
				\melodies2,
				Pbind(
					\instrument, \bpfsaw,
					\dur, Pwhite(0.5,3.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[74],[76],[78],[79],[81],[83]], 1),
							Pseq([[72],[74],[78],[76],[74],[78],[72]], 1),
							Pseq([[74],[72],[78],[76],[79],[81],[83]], 1),
							Pseq([[74],[72],[78]], 1),
							Pseq([[72],[78],[83]], 1),
							Pseq([[78]], 7),
						],
						inf
					),
					\detune, Pexprand(0.025,0.1),
					\cfmin, 150,
					\cfmax, 1200,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.25,2.75),
					\rel, Pwhite(7.5,11.25),
					\ldb, 6,
					\amp, 0.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReSecondMelody -> {
			Pdef(
				\melodies2,
				Pbind(
					\instrument, \bpfsaw,
					\dur, Pwhite(0.5,3.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[74],[76],[78],[79],[81],[83]], 1),
							Pseq([[72],[74],[78],[76],[74],[78],[72]], 1),
							Pseq([[74],[72],[78],[76],[79],[81],[83]], 1),
							Pseq([[74],[72],[78]], 1),
							Pseq([[72],[78],[83]], 1),
							Pseq([[78]], 7),
						],
						inf
					),
					\detune, Pexprand(0.025,0.1),
					\cfmin, 150,
					\cfmax, 1200,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.25,2.75),
					\rel, Pwhite(7.5,11.25),
					\ldb, 6,
					\amp, 0.5,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		melodies.add(\StopSecondMelody -> {
			Pdef(\melodies2).stop;
		});

		melodies.add(\ThirdMelody -> {
			Pdef(
				\melodies3,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pxrand( [Pseq([1/6], inf), Pseq([1/12], inf)],inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\scale_lydian_shorts][1],
							~buff[\scale_lydian_shorts][4],
							~buff[\scale_lydian_shorts][3],
							~buff[\scale_lydian_shorts][2],
							~buff[\scale_lydian_shorts][5],
							~buff[\scale_lydian_shorts][6],
							~buff[\scale_lydian_shorts][7],
							~buff[\scale_lydian_shorts][8],
							~buff[\scale_lydian_shorts][11],
							~buff[\scale_lydian_shorts][10],
							~buff[\scale_lydian_shorts][11],
							~buff[\scale_lydian_shorts][12],
						], 1
					),
					\amp, Pseq([2.5, 4.0], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).play(quant:params.actualTempo);
		});

		melodies.add(\ReThirdMelody -> {
			Pdef(
				\melodies3,
				Pbind(
					\instrument, \bpfbuf,
					\dur, Pxrand( [Pseq([1/6], inf), Pseq([1/12], inf)],inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\scale_lydian_shorts][1],
							~buff[\scale_lydian_shorts][4],
							~buff[\scale_lydian_shorts][3],
							~buff[\scale_lydian_shorts][2],
							~buff[\scale_lydian_shorts][5],
							~buff[\scale_lydian_shorts][6],
							~buff[\scale_lydian_shorts][7],
							~buff[\scale_lydian_shorts][8],
							~buff[\scale_lydian_shorts][11],
							~buff[\scale_lydian_shorts][10],
							~buff[\scale_lydian_shorts][11],
							~buff[\scale_lydian_shorts][12],
						], 1
					),
					\amp, Pseq([2.5, 4.0], inf),
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		melodies.add(\StopThirdMelody -> {
			Pdef(\melodies3).stop;
		});


		oneShots.add(\FirstOS -> {
			1.do{
				Synth(
					\bpfbuf,
					[
						\buf, ~buff[\sfx_crickets][0].bufnum,
						\amp, exprand(1.2,2.5),
						\pan, rrand(-0.9,0.9),
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
						\buf, ~buff[\sfx_wind][0].bufnum,
						\amp, exprand(2.2,4.5),
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
						\buf, ~buff[\sfx_magical][0].bufnum,
						\amp, exprand(16.2,20.4),
						\pan, rrand(-0.9,0.9),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});
	}
}