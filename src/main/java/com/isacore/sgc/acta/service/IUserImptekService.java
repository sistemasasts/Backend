package com.isacore.sgc.acta.service;

import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.util.CRUD;

public interface IUserImptekService extends CRUD<UserImptek>{	
	
	UserImptek findByUserImptek(String nickname);
	
	UserImptek findOnlyUserByNickname(String nicknamr);
}
