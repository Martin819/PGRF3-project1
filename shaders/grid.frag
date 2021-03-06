#version 330
in vec3 vertColor; // vstup z predchozi casti retezce
in vec3 normal;
in vec3 lightVec;
in vec3 eyeVec;
in vec2 vertInPosition;
out vec4 outColor;
uniform sampler2D texture;
uniform sampler2D textureNormal;
uniform sampler2D textureHeight;
uniform float lightPerVertex;
uniform float useTexture;
uniform float mappingType;
uniform float parallaxCoef;

void main() {
	vec3 fragColor;
	vec3 diffColor;
	vec3 norm = normal;
	vec2 textureCoords = vertInPosition;
	if(lightPerVertex == 0){
	    /* per pixel light */
		if(useTexture == 1){
			if(mappingType == 2){
				// parallax mapping		
				float height = texture2D(textureHeight, vertInPosition).r-0.5;
				float v = height * parallaxCoef - 0.007;
				vec3 eye = normalize(eyeVec);
				textureCoords = vertInPosition + (eye.xy * v).yx;
			}
			if(mappingType != 0){
				// normal mapping
				norm = texture2D(textureNormal, textureCoords).xyz;
				norm *= 2;
				norm -= 1;
			}
		}
		float diff = dot(norm, lightVec);
		diff = max(0,diff);
		vec3 halfVec = normalize(normalize(eyeVec) + lightVec);
		float spec = dot(norm, halfVec);
		spec = max(0,spec);
		spec = pow(spec,10);
		float ambient = 0.1;
		if(useTexture == 1){
			diffColor = texture2D(texture, textureCoords).rgb; 
		}else{
			diffColor = vec3(vertInPosition,0);
		}
		fragColor = diffColor * (min(ambient + diff,1)) + vec3(1,1,1) * spec;
	}else{
	    /* per vertex light*/
		if(useTexture == 1){
			diffColor = texture2D(texture, vertInPosition).rgb * vertColor; 
		}else{
			diffColor = vertColor;
		}
		fragColor = diffColor;
	}
	outColor = vec4(fragColor,1);
} 
