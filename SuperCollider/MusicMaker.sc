
MusicMaker {

	var <params, <server, <effects, <packages, <synths, <messages, <actualPackage;

	init{ arg package;
		var terror, ambient, desert, fantasy;

		packages = Dictionary.new;

		synths = Synths.new;
		synths.init;

		effects = Effects.new;
		effects.init;

		actualPackage = package;

		server = PluginServer.new;
		server.init;

		server.server.waitForBoot({
			server.server.sync;
			~unityClient = NetAddr.new("127.0.0.1", 7771);
			NetAddr.localAddr;
			server.boot = NetAddr.new("127.0.0.1", 7771);
			server.server.sync;

			server.server.sync;
			ServerTree.add(~makeNodes);
			server.server.freeAll;
			server.server.sync;

			server.server.sync;
			terror = TerrorPackage.new;
			terror.init();
			ambient = AmbientPackage.new;
			ambient.init();
			desert = DesertPackage.new;
			desert.init();
			fantasy = FantasyPackage.new;
			fantasy.init();
			server.server.sync;

			server.server.sync;
			packages.add(\Horror -> terror );
			packages.add(\Ambient -> ambient );
			packages.add(\Desert -> desert );
			packages.add(\Fantasy -> fantasy);
			server.server.sync;

			server.server.sync;
			messages = Messages.new;
			messages.init(packages[actualPackage]);
			server.server.sync;

			server.server.sync;
			//this.start;
			server.server.sync;
			"MusicMaker inicializado".postln;
		});
	}

	d_playRhythm{ arg layer;
		packages[actualPackage].playRhythm(layer);
	}

	d_stopRhythm{ arg layer;
		packages[actualPackage].stopRhythm(layer);
	}

	d_playHarmony{ arg layer;
		packages[actualPackage].playHarmony(layer);
	}

	d_stopHarmony{ arg layer;
		packages[actualPackage].stopHarmony(layer);
	}

	d_playMelody{ arg layer;
		packages[actualPackage].playMelody(layer);
	}

	d_stopMelody{ arg layer;
		packages[actualPackage].stopMelody(layer);
	}

	d_playOS{ arg layer;
		packages[actualPackage].playOS(layer);
	}

	d_setPackage{ arg pck;
		actualPackage = pck;
	}

	d_mulTempo { arg temp;
		packages[actualPackage].multiplyTempo(temp);
	}
}