import { useEffect, useState } from "react";
import axios from "axios";


const TestComponent = () => {
    const [text, setText] = useState("");

    useEffect(()=>{
        axios.get("http://localhost:9000").then((res)=> {
            console.log(res.data);
            setText(res.data);
        }).catch((err)=>{
            console.log(err);
        })

    },[])

    return(
    <div>
        {
            text? (
                <h1>{text}</h1>
            ) : <h1>NOT CONNECTED</h1>
        }    
    </div>
    )
}

export default TestComponent;