
AmbientPackage : Package {

	// Variables de la clase
	// El paquete no contempla cambios de tempo

		init {

		arg parameters;

		percs = Dictionary.new;
		chords = Dictionary.new;
		melodies = Dictionary.new;
		oneShots = Dictionary.new;

		params = MusicMakerParameters.new;
		params.init(1, 0);

		percs.add(\BasePercs -> {
			~marimba = Pbind(
				\instrument, \bpfsaw,
				\dur, Prand([1,0.5],inf),
				\freq, Prand([1/2,2/3,1,4/3,2,5/2,3,4,6,8],inf),
				\detune, Pwhite(0,0.1),
				\rqmin, 0.005,
				\rqmax, 0.008,
				\cfmin, Prand((Scale.major.degrees+64).midicps,inf) * Prand([0.5,1,2,4],inf),
				\cfmax, Pkey(\cfmin) * Pwhite(1.008,1.025),
				\atk, 3,
				\sus, 1,
				\rel, 5,
				\amp, 1,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		percs.add(\ReBasePercs -> {
			// ESTE PAQUETE NO CONTEMPLA LOS CAMBIOS DE TEMPO
			// PUESTO QUE SE BASA EN PATRONES PSEUDO-ALEATORIOS
		});

		percs.add(\StopBasePercs -> {
			~marimba.stop;
		});

		percs.add(\ExtraPercs -> {
			~bellCloud = Pbind(
				\instrument, \bpfbuf,
				\dur, Pexprand(0.2,2),
				\atk, Pexprand(0.5,2),
				\rel, Pexprand(2,6),
				\buf, ~buff[\percs_deskbells][0],
				\rate, Pwhite(-7.5,-5.5).midiratio,
				\spos, Pwhite(5000,80000),
				\amp, Pexprand(2,5),
				\bpfmix, 0,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		percs.add(\ReExtraPercs -> {
			// ESTE PAQUETE NO CONTEMPLA LOS CAMBIOS DE TEMPO
			// PUESTO QUE SE BASA EN PATRONES PSEUDO-ALEATORIOS
		});

		percs.add(\StopExtraPercs -> {
			~bellCloud.stop;
		});

		percs.add(\EffectsPercs -> {
			~drone = Pbind(
				\instrument, \bpfsaw,
				\dur, 1,
				\freq, 26.midicps,
				\detune, Pwhite(0.03,0.2),
				\rqmin, 0.08,
				\rqmax, 0.12,
				\cfmin, 50,
				\cfmax, 400,
				\atk, 2,
				\sus, 0.1,
				\rel, 2,
				\amp, 0.8,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		percs.add(\ReEffectsPercs -> {
			// ESTE PAQUETE NO CONTEMPLA LOS CAMBIOS DE TEMPO
			// PUESTO QUE SE BASA EN PATRONES PSEUDO-ALEATORIOS
		});

		percs.add(\StopEffectsPercs -> {
			~drone.stop;
		});


		chords.add(\BaseHarmony -> {
			~chords = Pbind(
				\instrument, \bpfsaw,
				\dur, Pwhite(4.5,7.0),
				\midinote, Pxrand([
					[23,35,54,63,64],
					[45,52,54,59,61,64],
					[28,40,47,56,59,63],
					[42,52,57,61,63]
				], inf),
				\detune, Pexprand(0.05,0.2),
				\cfmin, 100,
				\cfmax, 1500,
				\rqmin, Pexprand(0.01,0.15),
				\atk, Pwhite(2.0,2.5),
				\rel, Pwhite(6.5,10.0),
				\ldb, 6,
				\amp, 0.2,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		chords.add(\ReBaseHarmony -> {
			// ESTE PAQUETE NO CONTEMPLA LOS CAMBIOS DE TEMPO
			// PUESTO QUE SE BASA EN PATRONES PSEUDO-ALEATORIOS
		});

		chords.add(\StopBaseHarmony -> {
			~chords.stop;
		});

		chords.add(\ExtraHarmony -> {
			~chords2 = Pbind(
				\instrument, \bpfsaw,
				\dur, Pwhite(7.5,1.0),
				\midinote, Pxrand([
					[42,54,75,76],
				], inf),
				\cfmin, 300,
				\cfmax, 1000,
				\rqmin, Pexprand(0.01,0.15),
				\atk, Pwhite(2.0,2.5),
				\rel, Pwhite(6.5,10.0),
				\ldb, 6,
				\amp, 0.35,
				\group, ~mainGrp,
				\out, ~bus[\reverb],
			).play;
		});

		chords.add(\ReExtraHarmony -> {
			// ESTE PAQUETE NO CONTEMPLA LOS CAMBIOS DE TEMPO
			// PUESTO QUE SE BASA EN PATRONES PSEUDO-ALEATORIOS
		});

		chords.add(\StopExtraHarmony -> {
			~chords2.stop;
		});


		melodies.add(\FirstMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\ReFirstMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\StopFirstMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\SecondMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\ReSecondMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\StopSecondMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\ThirdMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\ReThirdMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
		});

		melodies.add(\StopThirdMelody -> {
			// ESTE PAQUETE NO CONTEMPLA LINEAS MELODICAS
			// PUESTO QUE NO SE ADAPTAN A LO QUE BUSCAMOS COMO MUSICA AMBIENTAL
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
						\amp, exprand(0.5,0.9),
						\pan, rrand(-0.5,0.5),
						\out, ~bus[\reverb],
					],
					~mainGrp
				);
			};
		});

		oneShots.add(\SecondOS -> {
			15.do{
				Synth(
					\bpfbuf,
					[
						\atk, rrand(0.2,3.0),
						\sus, rrand(0.2,2.0),
						\rel, exprand(1.0,6.0),
						\c1, exprand(1,8),
						\c2, exprand(-8,-1),
						\buf, ~buff[\percs_shakers][13].bufnum,
						\rate, exprand(0.4,2.0),
						\bpfmix, 0,
						\amp, exprand(0.1,0.2),
						\pan, rrand(-0.9,0.9),
						\spos, rrand(0,100000),
						\out, ~bus[\reverb]
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
						\amp, exprand(0.2,1.5),
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