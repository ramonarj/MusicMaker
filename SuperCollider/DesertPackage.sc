
DesertPackage : Package {

	// Variables de la clase
	var <tempo = 1.875; // 128bpm -> 128/60bps -> 60/128spb -> 60/128*4spc = 1.875

		init {

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
					\dur, Pseq([1/8], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_african_low], 1),
							Prand(~buff[\percs_african_high], 7),
							Prand(~buff[\percs_african_low], 1),
							Prand(~buff[\percs_african_high], 7),
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.4, 0.8, 7)], inf),
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
					\dur, Pseq([1/8], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							Prand(~buff[\percs_african_low], 1),
							Prand(~buff[\percs_african_high], 7),
							Prand(~buff[\percs_african_low], 1),
							Prand(~buff[\percs_african_high], 7),
						], inf
					),
					\amp, Pseq([1.4, Pexprand(0.4, 0.8, 7)], inf),
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
					\dur, Pseq([1/8], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_african_mid][0],
							~buff[\percs_african_mid][0],
							~buff[\percs_african_mid][0],
							Prand(~buff[\percs_african_mid], 5),
						], inf
					),
					\amp, 1.0,
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
					\dur, Pseq([1/8], inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\percs_african_mid][0],
							~buff[\percs_african_mid][0],
							~buff[\percs_african_mid][0],
							Prand(~buff[\percs_african_mid], 5),
						], inf
					),
					\amp, 3.0,
					\group, ~mainGrp,
					\out, ~bus[\reverb],
				);
			).quant_(params.actualTempo);
		});

		percs.add(\StopExtraPercs -> {
			Pdef(\rhythm2).stop;
		});

		percs.add(\EffectsPercs -> {
			~shakerSustain = Pbind(
				\instrument, \bpfbuf,
				\dur, Pwhite(0.2,0.7),
				\atk, Pexprand(2,4),
				\rel, Pexprand(3,5),
				\buf, ~buff[\percs_shakers][13].bufnum,
				\rate, Pwhite(-7.0,-4.0).midiratio,
				\spos, Pwhite(0, ~buff[\percs_shakers][13].numFrames/2),
				\amp, Pexprand(0.5,1.0),
				\freq, {rrand(85.0,105.0).midicps}!3,
				\rq, 0.005,
				\bpfmix, 0.97,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		percs.add(\ReEffectsPercs -> {
			// ESTE PATRON NO SE VE AFECTADO POR EL TEMPO
			// PUESTO QUE ES UN SUSTAIN MANTENIDO
		});

		percs.add(\StopEffectsPercs -> {
			~shakerSustain.stop;
		});

		chords.add(\BaseHarmony -> {
			Pdef(
				\chord,
				Pbind(
					\instrument, \bpfsaw,
					\dur, 5.0,
					\midinote, Pxrand([
						[48, 52, 55]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 1500,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.8,
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
						[48, 52, 55]
					], inf),
					\detune, Pexprand(0.05,0.1),
					\cfmin, 100,
					\cfmax, 1500,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.8,
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
					\dur, 3.0,
					\midinote, Pxrand([
						[60, 64, 67],
						[61, 64, 68, 71]
					], inf),
					\detune, Pexprand(0.015,0.020),
					\cfmin, 300,
					\cfmax, 1000,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(4.5,7.0),
					\ldb, 6,
					\amp, 0.8,
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
					\dur, 3.0,
					\midinote, Pxrand([
						[60, 64, 67],
						[61, 64, 68, 71]
					], inf),
					\detune, Pexprand(0.015,0.020),
					\cfmin, 300,
					\cfmax, 1000,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(4.5,7.0),
					\ldb, 6,
					\amp, 0.8,
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
							Pxrand(~buff[\scale_double_harmonic], 1),
						], inf
					),
					\rel, Pwhite(10.0),
					\amp, Pseq([3.8, 5.0], inf),
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
							Pxrand(~buff[\scale_double_harmonic], 1),
						], inf
					),
					\rel, Pwhite(10.0),
					\amp, Pseq([3.8, 5.0], inf),
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
					\dur, Pwhite(0.5,5.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[73],[76],[77],[79],[80],[82]], 1),
							Pseq([[73],[72],[73],[77],[79],[80],[82]], 1),
							Pseq([[82],[80],[79],[77],[79],[80],[82]], 1),
							Pseq([[72]], 7),
							Pseq([[72]], 4),
						],
						inf
					),
					\detune, Pexprand(0.05,0.2),
					\cfmin, 100,
					\cfmax, 1500,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.7,
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
					\dur, Pwhite(0.5,5.0),
					\midinote, Pxrand
					(
						[
							Pseq([[72],[73],[76],[77],[79],[80],[82]], 1),
							Pseq([[73],[72],[73],[77],[79],[80],[82]], 1),
							Pseq([[82],[80],[79],[77],[79],[80],[82]], 1),
							Pseq([[72]], 7),
							Pseq([[72]], 4),
						],
						inf
					),
					\detune, Pexprand(0.05,0.2),
					\cfmin, 100,
					\cfmax, 1500,
					\rqmin, Pexprand(0.01,0.15),
					\atk, Pwhite(2.0,2.5),
					\rel, Pwhite(6.5,10.0),
					\ldb, 6,
					\amp, 0.7,
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
					\dur, Pxrand( [Pseq([1/8], inf), Pseq([1/12], inf)],inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\scale_double_harmonic_shorts][1],
							~buff[\scale_double_harmonic_shorts][2],
							~buff[\scale_double_harmonic_shorts][3],
							~buff[\scale_double_harmonic_shorts][4],
							~buff[\scale_double_harmonic_shorts][5],
							~buff[\scale_double_harmonic_shorts][6],
							~buff[\scale_double_harmonic_shorts][7],
							~buff[\scale_double_harmonic_shorts][8],
							~buff[\scale_double_harmonic_shorts][9],
							~buff[\scale_double_harmonic_shorts][10],
							~buff[\scale_double_harmonic_shorts][11],
							~buff[\scale_double_harmonic_shorts][12],
						], 1
					),
					\amp, Pseq([3.8, 5.0], inf),
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
					\dur, Pxrand( [Pseq([1/8], inf), Pseq([1/12], inf)],inf),
					\stretch, params.actualTempo,
					\buf, Pseq(
						[
							~buff[\scale_double_harmonic_shorts][1],
							~buff[\scale_double_harmonic_shorts][2],
							~buff[\scale_double_harmonic_shorts][3],
							~buff[\scale_double_harmonic_shorts][4],
							~buff[\scale_double_harmonic_shorts][5],
							~buff[\scale_double_harmonic_shorts][6],
							~buff[\scale_double_harmonic_shorts][7],
							~buff[\scale_double_harmonic_shorts][8],
							~buff[\scale_double_harmonic_shorts][9],
							~buff[\scale_double_harmonic_shorts][10],
							~buff[\scale_double_harmonic_shorts][11],
							~buff[\scale_double_harmonic_shorts][12],
						], 1
					),
					\amp, Pseq([3.8, 5.0], inf),
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
						\buf, ~buff[\sfx_dh_slides][1].bufnum,
						\amp, exprand(3.5,5.5),
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
						\buf, ~buff[\sfx_dh_slides][0].bufnum,
						\amp, exprand(3.5,5.5),
						\pan, rrand(-0.9,0.9),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});

		oneShots.add(\ThirdOS -> {
			15.do{
				Synth(
					\bpfbuf,
					[
						\atk, rrand(0.1,2.0),
						\sus, rrand(2.5,6.0),
						\rel, exprand(1.0,5.0),
						\c1, exprand(1,8),
						\c2, exprand(-8,-1),
						\buf, ~buff[\percs_shakers][13].bufnum,
						\rate, exprand(0.3,1.2),
						\freq, (Scale.major.degrees.choose+64 + [-12,0,12,24].choose).midicps,
						\rq, exprand(0.002,0.02),
						\bpfmix, 1,
						\amp, exprand(2.2,4.5),
						\pan, rrand(-0.9,0.9),
						\spos, rrand(0,100000),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});
	}
}