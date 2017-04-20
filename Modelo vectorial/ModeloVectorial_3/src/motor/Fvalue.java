package motor;

public class Fvalue {
	public float calcFvalue(Recall r, Precision p){
		float f,recall =r.getRcall(),precision = p.getPrecision();
		f=(2*recall*precision)/(recall+precision);
		return f;
	}
}
