//CONSULTAS:
    //Usuarios cuyo genero sea Masculino:
        db.Usuario.find({gender:"Masculine"});
    //Usuarios cuyo genero sea Femenino:
        db.Usuario.find({gender:"Feminine"});
    //Usuarios cuyo lenguaje sea Inglés:
        db.Usuario.find({preferred_language:"English"});
    //Usuarios cuyo lenguaje sea Español:
        db.Usuario.find({preferred_language:"Spanish"});
    //Usuarios cuyo lenguaje sea Alemán:
        db.Usuario.find({preferred_language:"German"});
    //Usuarios cuyo lenguaje sea Francés:
        db.Usuario.find({preferred_language:"French"});
    //Usuarios cuyo role sea Administrador:
        db.Usuario.find({role:"admin"});
    //Usuarios cuyo role sea Moderador:
        db.Usuario.find({role:"mod"});
    //Usuarios cuyo role sea Usuario:
        db.Usuario.find({role:"user"});
    //Usuarios ordenados alfabéticamente por nombre:
        db.Usuario.find().sort({display_name:1});
    //Cambiar el genero de los usuario de Masculine a Male:
        db.Usuario.update({gender:"Masculine"}, {$set: {gender:"Male"}}, {multi: true});
    //Cambiar el genero de los usuario de Feminine a Female:
        db.Usuario.update({gender:"Feminine"}, {$set: {gender:"Female"}}, {multi: true});
