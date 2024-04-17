console.log("js for task 9 running"); 

const ce = React.createElement; 
const csrfToken = document.getElementById("csrfToken").value; 
const loginRoute = document.getElementById("loginRoute").value; 
const createRoute = document.getElementById("createRoute").value;
const getGlobalMessagesRoute = document.getElementById("getGlobalMessagesRoute").value;  
const getLocalMessagesRoute = document.getElementById("getLocalMessagesRoute").value;
const sendGlobalMessageRoute = document.getElementById("sendGlobalMessageRoute").value;  
const sendLocalMessageRoute = document.getElementById("sendLocalMessageRoute").value;  

class Task8MainComponent extends React.Component {
    constructor(props){
        super(props)
        this.state = {loggedIn: false}
    }
    render(){
        if(this.state.loggedIn){
            return ce(MessagesComponent, {doLogout: ()=> this.setState({loggedIn: false})}, null); 
            //WITH SET STATE TO FALSE FOR DO LOGUT  
        } else{
            return ce(LoginComponent, {doLogin: ()=> this.setState({loggedIn: true})}, null); 
        }
    }
}
class LoginComponent extends React.Component{
    constructor(props){
        super(props); 
        this.state={
            loginName: "", 
            loginPass: "",
            createName: "", 
            createPass: "", 
            loginMessage: "", 
            createMessage:""
        }
    }
   
    render(){
        return ce('div', null,
        ce('p', null, 'Task 9 w/ react and a database!'),
        ce('h2', null, 'Login:'),
        ce('br'),
        'Username: ',
        ce('input', { type: "text", id: "loginName", value: this.state.loginName, onChange: e => this.changerHandler(e) }),
        ce('br'),
        'Password: ',
        ce('input', { type: "password", id: "loginPass", value: this.state.loginPass, onChange: e => this.changerHandler(e) }),
        ce('br'),
        ce('button', { onClick: e => this.login(e) }, 'Login'),
        ce('span', {id: "login-message"}, this.state.loginMessage), 
        ce('h2', null, 'Create User: '),
        ce('br'),
        'Username: ',
        ce('input', { type: "text", id: "createName", value: this.state.createName, onChange: e => this.changerHandler(e) }),
        ce('br'),
        'Password: ',
        ce('input', { type: "password", id: "createPass", value: this.state.createPass, onChange: e => this.changerHandler(e) }),
        ce('br'),
        ce('button', { onClick: e => this.createUser(e) }, 'Create User'),
        ce('span', {id: "create-message"}, this.state.createMessage), 
    );
    }
    changerHandler(e) {
        this.setState({ [e.target['id']]: e.target.value });
    }
    login(e){
        const username = this.state.loginName; 
        const password = this.state.loginPass; 
        fetch(loginRoute, {
            method: 'POST', 
            headers: { 'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
            body: JSON.stringify({username, password})
        }).then(res => res.json()).then(data => {
           if(data){
            this.props.doLogin(); 
           } else{
            this.setState({loginMessage: "Login Failed"})
           }
        }); 
    }
    createUser(e){
        const username = this.state.createName; 
        const password = this.state.createPass; 
        fetch(createRoute, {
            method: 'POST', 
            headers: { 'Content-Type': 'application/json', 'Csrf-Token': csrfToken}, 
            body: JSON.stringify({username, password})
        }).then(res => res.json()).then(data =>{
            if(data){
                this.props.doLogin(); 
            } else {
                this.setState({createMessage: "Create User Failed"})
            }
        })
    }
}

class MessagesComponent extends React.Component{
    constructor(props){
        super(props); 
        this.state = {globalMessages: [], localMessages: [], 
                      sendGlobalForm: "", sendLocalUsername: "",
                      sendLocalContents:"",
                      sendMessageFeedback: ""}
    }
    componentDidMount(){
        this.getGlobalMessages(); 
        this.getLocalMessages(); 
    }
    render(){
        return ce('div', null, 
        ce('div',  {id:"seeGlobal"}, 
            ce('h2', null, "Global Messages:"),
            ce('ul', null, this.state.globalMessages.map((globalMessage) => ce('li', null, globalMessage.sender + " says " + globalMessage.content)))
            ), //end see global div  
        ce('div', {id: "seeLocal"}, 
            ce('h2', null, "Local Messages:"),
            ce('ul', null, this.state.localMessages.map((localMessage) => ce('li', null, localMessage.sender + " says " + localMessage.contents)))
            ),  //end see local div 
        ce('div', {id:"sendGlobal"}, 
            ce('input', {type: "text", value:this.state.sendGlobalForm,id:"sendGlobalForm", onChange: e=>this.handleChange(e)}), 
            ce('br'), 
            ce('button', {onClick: e=> this.sendGlobalMessage(e)},"Send Global Message"), 
            ), //end send global div 
        ce('div', {id: "sendLocal"}, 
            "Recipient username", 
            ce('input', {type: "text", value:this.state.sendLocalUsername,id:"sendLocalUsername", onChange: e=>this.handleChange(e)}),
            ce('br'), 
            "Message contents", 
            ce('input', {type:"text", value:this.state.sendLocalContents, id:"sendLocalContents", onChange: e=> this.handleChange(e)}), 
            ce('br'), 
            ce('button', {onClick: e=> this.sendLocalMessage(e)}, "Send Local Message")),
            ce('p', null, this.state.sendMessageFeedback),
            ce('button', {onClick: e=> this.logout(e)}, "Logout")
            );  
    }
    getGlobalMessages(){
        fetch(getGlobalMessagesRoute).then(res=>res.json()).then(globalMessages=>{
         this.setState({ globalMessages })
        });
    }
    getLocalMessages(){
        fetch(getLocalMessagesRoute).then(res=>res.json()).then(localMessages =>{
            console.log(localMessages)
            this.setState({ localMessages })
        }); 
    }
    sendGlobalMessage(e){
        fetch(sendGlobalMessageRoute, {
            method: 'POST', 
            headers: {'Content-Type' : 'application/json', 'Csrf-Token' : csrfToken}, 
            body: JSON.stringify(this.state.sendGlobalForm)
        }).then(res => res.json()).then(data=>{
            if(data){
                this.getGlobalMessages(); 
                this.setState({sendGlobalForm: "", sendMessageFeedback: "Global Message Sent!"}); 
            } else {
                this.setState({sendMessageFeedback: "There was an error"})
            }
        })
    }
    sendLocalMessage(e){ 
        const reciever = this.state.sendLocalUsername
        const contents = this.state.sendLocalContents
        fetch(sendLocalMessageRoute, {
            method: 'POST', 
            headers: {'Content-Type' : 'application/json', 'Csrf-Token' : csrfToken}, 
            body: JSON.stringify({reciever, contents})
        }).then(res=>res.text()).then(data=>{
            if(data === "true"){ // i love javascript 
                this.getLocalMessages(); 
                this.setState({sendLocalUsername: "", sendLocalContents: "", sendMessageFeedback: "Local Message Sent!"}); 
            }else{
                this.setState({sendMessageFeedback: "There was an error"})
            }
        })
    }
    handleChange(e){
        this.setState({[e.target['id']]: e.target.value})
    }
    logout(e){
        this.props.doLogout(); 
    }
  

}
ReactDOM.render(
    ce(Task8MainComponent, null, null), document.getElementById('react-root'));

