
Synths {

	init{

		~synths = {
			//SINTE DE DIENTES DE SIERRA, GENERICO PARA MUCHOS USOS
			SynthDef(\bpfsaw, {
				arg atk=2, sus=0, rel=3, c1=1, c2=(-1),
				freq=500, detune=0.2, pan=0, cfhzmin=0.1, cfhzmax=0.3,
				cfmin=500, cfmax=2000, rqmin=0.1, rqmax=0.2,
				lsf=200, ldb=0, amp=1, out=0;
				var sig, env;
				env = EnvGen.kr(Env([0,1,1,0],[atk,sus,rel],[c1,0,c2]),doneAction:2);
				sig = Saw.ar(freq * {LFNoise1.kr(0.5,detune).midiratio}!2);
				sig = BPF.ar(
					sig,
					{LFNoise1.kr(
						LFNoise1.kr(4).exprange(cfhzmin,cfhzmax)
					).exprange(cfmin,cfmax)}!2,
					{LFNoise1.kr(0.1).exprange(rqmin,rqmax)}!2
				);
				sig = BLowShelf.ar(sig, lsf, 0.5, ldb);
				sig = Balance2.ar(sig[0], sig[1], pan);
				sig = sig * env * amp;
				Out.ar(out, sig);
			}).add;

			//SAMPLER
			SynthDef(\bpfbuf, {
				arg atk=0, sus=0, rel=3, c1=1, c2=(-1),
				buf=0, rate=1, spos=0, freq=440, rq=1, bpfmix=0,
				pan=0, amp=1, out=0;
				var sig, env;
				env = EnvGen.kr(Env([0,1,1,0],[atk,sus,rel],[c1,0,c2]),doneAction:2);
				sig = PlayBuf.ar(1, buf, rate*BufRateScale.ir(buf),startPos:spos);
				sig = XFade2.ar(sig, BPF.ar(sig, freq, rq, 1/rq.sqrt), bpfmix*2-1);
				sig = sig * env;
				sig = Pan2.ar(sig, pan, amp);
				Out.ar(out, sig);
			}).add;

			//ORGANO
			SynthDef(\organDonor,{
				arg out = 0, pan = 0.0, freq = 440, amp = 0.1, gate = 1, att = 0.01, dec = 0.5, sus = 1,
				rel = 0.5, lforate = 10, lfowidth = 0.01, cutoff = 100, rq = 0.5;
				var vibrato, pulse, filter, env;
				vibrato = SinOsc.ar(lforate, Rand(0, 2.0));
				// up octave, detune by 4 cents
				// 11.96.midiratio = 1.9953843530485
				// up octave and a half, detune up by 10 cents
				// 19.10.midiratio = 3.0139733629359
				freq = freq * [1, 1.9953843530485, 3.0139733629359];
				freq = freq * (1.0 + (lfowidth * vibrato));
				pulse = VarSaw.ar(
					freq: freq,
					iphase: Rand(0.0, 1.0) ! 3,
					width: Rand(0.3, 0.5) ! 3,
					mul: [1.0,0.7,0.3]);
				pulse = Mix(pulse);
				filter = RLPF.ar(pulse, cutoff, rq);
				env = EnvGen.ar(
					envelope: Env.adsr(att, dec, sus, rel, amp),
					gate: gate,
					doneAction: 2);
				Out.ar(out, Pan2.ar(filter * env, pan));
			}).add;
		}

	}
}