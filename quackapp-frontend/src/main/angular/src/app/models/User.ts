export class User {
    id: number;
    username: string;
    email: string;
    passwordHash?: string;
    realName?: string;
    birthday?: Date;
    signUpTimestamp: Date;
    lastActiveTimestamp: Date;
    admin: boolean;
    moderator: boolean;

    static fromObject(object: any) : User {
        const user = new User();
        user.id = object.id;
        user.username = object.username;
        user.email = object.email;
        user.signUpTimestamp = new Date(parseInt(object.signUpTimestamp));
        user.admin = object.admin;
        user.moderator = object.moderator;

        if (object.realName !== undefined) user.realName = object.realName;
        if (object.birthday !== undefined) user.birthday = new Date(parseInt(object.birthday));
        if (object.lastActiveTimestamp !== undefined) user.lastActiveTimestamp = new Date(parseInt(object.lastActiveTimestamp));

        if (object.passwordHash !== undefined) user.passwordHash = object.passwordHash;
        return user;
    }
}
