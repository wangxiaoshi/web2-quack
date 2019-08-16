import {User} from "./User";

export class Quack {
    id: number;
    title: string;
    text: string;
    date: Date;
    publiclyVisible: boolean;
    bg: string;

    author: User;

    static fromObject(object: any) : Quack {
        const quack = new Quack();
        quack.id = object.id;
        quack.title = object.title;
        quack.text = object.text;
        quack.date = new Date(parseInt(object.date));
        quack.publiclyVisible = object.publiclyVisible;
        quack.bg = object.backgroundColor;
        quack.author = User.fromObject(object.author);
        return quack;
    }
}
