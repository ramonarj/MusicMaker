
Messages {
	var params, package;

	init{ arg pack;

		pack.postln;
		package = pack;

		//Reproducir la música
		OSCdef.new(\Play,
			{
				arg msg;
				"Recibido mensaje /Play".postln;

		},'/Play', nil, 57120);

		//Parar la música
		OSCdef.new(\Stop,
			{
				arg msg;
				"Recibido mensaje /Stop".postln;
				package.stopAll();

		},'/Stop', nil, 57120);

		//Activar una capa en concreto
		OSCdef.new(\PlayLayer,
			{
				arg msg;
				var stringMsg = "lol";
				var params;
				"Recibido mensaje /PlayLayer".postln;

				//Conversión a string
				stringMsg = msg[1];
				stringMsg = stringMsg.asString();

				//Split del string
				params = stringMsg.split($ );

				//Vemos qué capa hay que activar
				switch (params[0])
				{"Rhythm"}   { package.playRhythm(params[1].asInteger()) }
				{"Harmony"} { package.playHarmony(params[1].asInteger()) }
				{"Melody"} { package.playMelody(params[1].asInteger()) }
				{"FX"} { package.playOS(params[1].asInteger()) }


		},'/PlayLayer', nil, 57120);

		//Desactivar una capa en concreto
		OSCdef.new(\StopLayer,
			{
				arg msg;
				var stringMsg = "lol";
				var params;
				"Recibido mensaje /StopLayer".postln;

				//Conversión a string
				stringMsg = msg[1];
				stringMsg = stringMsg.asString();

				//Split del string
				params = stringMsg.split($ );

				//Vemos qué capa hay que activar
				switch (params[0])
				{"Rhythm"}   { package.stopRhythm(params[1].asInteger()) }
				{"Harmony"} { package.stopHarmony(params[1].asInteger()) }
				{"Melody"} { package.stopMelody(params[1].asInteger()) }
				{"FX"} { "Los OS se paran solos".postln }


		},'/StopLayer', nil, 57120);
	}
}
