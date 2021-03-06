#version 330
#define PI 3.14159265359
in vec2 inPos;
out vec3 normal;
out vec3 lightVec;
out vec3 eyeVec;
out vec3 vertColor;
out vec2 vertInPosition; 
uniform mat4 mat;
uniform vec3 lightPos;
uniform vec3 eyePos;
uniform float renderFunction;
uniform float lightPerVertex;
uniform float useTexture;
uniform float useAnimation;
uniform float mappingType;
uniform float time;

/* function definitions */
/* cartesian */
vec3 funcSaddle(vec2 inPos) {
	float s = inPos.x * 2 - 1;
	float t = inPos.y * 2 - 1;
	return vec3(s,t,s*s - t*t);
}
vec3 funcCone(vec2 inPos){
	float s = (PI * 0.5 - PI * inPos.x *2);
	float t = inPos.y * 2 -1;

	return vec3(t*cos(s),
				t*sin(s),
				t
	);
}

vec3 funcSnake(vec2 inPos){
	float s = inPos.x;
	float t = (2 * PI * inPos.y);
	return vec3((1-s)*(3+cos(t))*cos(2*PI*s),(1-s)*(3+cos(t))*sin(2*PI*s),6*s+(1-s)*sin(t))/3;
}

/* cylidric */

vec3 funcMushroom(vec2 inPos){
	float s= PI * 0.5 - PI * inPos.x * 2;
	float t = PI * inPos.y * 2;
	
	float r = (1+max(sin(t),0))*2;
	float theta = s;	
    return vec3(
    	r*cos(theta),
    	r*sin(theta),
    	3-t)/2;
}

vec3 funcSombrero(vec2 inPos){
	float s = (PI * 0.5 - PI * inPos.x *2);
	float t = (2 * PI * inPos.y);
	float r = t;
	float theta = s;
	return vec3(
			r*cos(theta),
			r*sin(theta),
			2*sin(t))/2;
}

vec3 funcGlass(vec2 inPos){
	float s= PI * 0.5 - PI * inPos.x * 2;
	float t= PI * 0.5 - PI * inPos.y * 2;
	
	float r = 1 + cos(t);
	float theta = s;
	
	return vec3(
		r*cos(theta),
		r*sin(theta),
		t);
}

/* sferical */
/* e^(-(t-pi/16)^4) baloon */
/* sin(pi*((x)^2+(y)^2))/2 explosion */
vec3 funcSphere(vec2 inPos){
	float s = PI * 0.5 - 2*PI * inPos.x;
	float t = PI * inPos.y;
	float r = 2;	
	return vec3(cos(t) * cos(s) * r,
			sin(t) * cos(s) * r, 
			sin(s) * r);
} 

vec3 funcFountain(vec2 inPos){
	float s = 2*PI * inPos.x;
	float t = PI * inPos.y;
	
	float r = cos(t*2)*sin(PI/4*cos(t*4));
	float phi = t;
    float theta = s;
    
	return vec3(cos(theta) * sin(phi) * r,
			sin(theta) * sin(phi) * r,
			cos(phi) * r);
} 

vec3 funcElephantHead(vec2 inPos){
	float s = PI * 0.5 - 2*PI * inPos.x;
	float t = PI * inPos.y;
	
	float r = 3+cos(4*s);
	float phi = t;
	float theta = s;
	
	return vec3(cos(theta) * sin(phi) * r,
			sin(theta) * sin(phi) * r, 
			cos(phi) * r)/2;
}

vec3 funcRainDrop(vec2 inPos){
	float s = PI * 0.5 - 2*PI * inPos.x;
	float t = (PI * inPos.y)/2;

	float r = -(t-PI/2);
	float phi = t;
	float theta = s;

	return vec3(cos(theta) * sin(phi) * r,
			sin(theta) * sin(phi) * r,
			cos(phi) * r);
}

vec3 funcAlien(vec2 inPos){
	float s = PI * 0.5 - 2*PI * inPos.x;
	float t = PI * inPos.y;
	
	float r=2+cos(2*t)*sin(s);
	float phi = t;
	float theta = s;

	return vec3(cos(theta) * sin(phi) * r,
			sin(theta) * sin(phi) * r,
			cos(phi) * r)/2;
}

vec3 funcRose(vec2 inPos){
	float s = PI * 0.5 - 2*PI * inPos.x;
	float t = PI * inPos.y;

	float r=2+cos(12*t)*sin(s);
	float phi = t;
	float theta = s;

	return vec3(cos(theta) * sin(phi) * r,
			sin(theta) * sin(phi) * r,
			cos(phi) * r)/2;
}

/* fucntion selector */
vec3 paramPos(vec2 inPos){
    float w = cos(time) * 0.5 + 0.5;
	if(renderFunction == 0){
        if(useAnimation == 0){
            return funcSaddle(inPos);
        }
        else{
            return w * funcSaddle(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 1){
        if(useAnimation == 0){
            return funcSnake(inPos);
        }
        else{
            return w * funcSnake(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 2){
        if(useAnimation == 0){
            return funcCone(inPos);
        }
        else{
            return w * funcCone(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 3){
        if(useAnimation == 0){
            return funcSombrero(inPos);
        }
        else{
            return w * funcSombrero(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 4){
        if(useAnimation == 0){
            return funcMushroom(inPos);
        }
        else{
            return w * funcMushroom(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 5){
        if(useAnimation == 0){
            return funcGlass(inPos);
        }
        else{
            return w * funcGlass(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 6){
        if(useAnimation == 0){
            return funcSphere(inPos);
        }
        else{
            return w * funcSphere(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 7){
        if(useAnimation == 0){
            return funcElephantHead(inPos);
        }
        else{
            return w * funcElephantHead(inPos) + (1 - w) * funcSphere(inPos);
        }
	}else if(renderFunction == 8){
        if(useAnimation == 0){
            return funcAlien(inPos);
        }
        else{
            return w * funcAlien(inPos) + (1 - w) * funcSphere(inPos);
        }
    }else if(renderFunction == 9){
        if(useAnimation == 0){
            return funcRose(inPos);
        }
        else{
            return w * funcRose(inPos) + (1 - w) * funcSphere(inPos);
        }
    }else if(renderFunction == 10){
        if(useAnimation == 0){
            return funcRainDrop(inPos);
        }
        else{
            return w * funcRainDrop(inPos) + (1 - w) * funcSphere(inPos);
        }
    }else if(renderFunction == 11){
        if(useAnimation == 0){
            return funcFountain(inPos);
        }
        else{
            return w * funcFountain(inPos) + (1 - w) * funcSphere(inPos);
        }
    }
}
vec3 paramNormal(vec2 inPos) {
   float delta = 0.001; 
   vec3 tx = (paramPos(inPos + vec2(delta, 0)) - paramPos(inPos - vec2(delta, 0)));
   vec3 ty = (paramPos(inPos + vec2(0, delta)) - paramPos(inPos - vec2(0, delta)));
   return cross(tx,ty);
} 
mat3 paramTangent(vec2 inPos){
	float delta = 0.001;
	vec3 tx = paramPos(inPos + vec2(delta,0)) - paramPos(inPos - vec2(delta,0));
	vec3 ty = paramPos(inPos + vec2(0,delta)) - paramPos(inPos - vec2(0,delta));
	tx= normalize(tx);
	ty = normalize(ty);
	vec3 tz = cross(tx,ty);
	ty = cross(tz,tx);
	return mat3(tx,ty,tz);
}
void main() {
	vec3 position = paramPos(inPos);
	gl_Position = mat * vec4(position,1.0);
	normal = normalize(paramNormal(inPos));
	lightVec = normalize(lightPos - position);
	vertInPosition = inPos;
	eyeVec = normalize(eyePos - position);
	if(mappingType==2){
		/* parallax mapping */
		mat3 tbn = paramTangent(inPos);
	}
	
	if(lightPerVertex == 1){
		/* per vertex light */
		vec3 norm = normal;
		float diff = dot(norm, lightVec);
		diff = max(0,diff);
		vec3 halfVec = normalize(normalize(eyeVec) + lightVec);
		float spec = dot(norm, halfVec);
		spec = max(0,spec);
		spec = pow(spec,10);
		float ambient = 0.1;
		if(useTexture == 1){
			vertColor=vec3(1,1,1) * (min(ambient + diff,1)) + vec3(1,1,1) * spec; 
		}else{
			vertColor=vec3(inPos,0) * (min(ambient + diff,1)) + vec3(1,1,1) * spec;
		}
	}
}
/*
float w = cos(time) * 0.5 + 0.5;
return w * sphere(paramPos) + (1 - w) * something(paramPos);
*/