from flask import Flask, jsonify, render_template, request
from flask_restful import Api,Resource,reqparse
from flask_cors import CORS

# Initialize Flask
app = Flask(__name__,static_folder='static',template_folder='static',static_url_path="")
api = Api(app)

# Cross Domain
cors = CORS(app, resources={r"/*": {"origins": "*"}})

# parse parameters
parser = reqparse.RequestParser()
parser.add_argument('key', type=str)

# Basic Route
@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'GET':
        return render_template('index.html')
    elif request.method == 'POST':
        key = request.args.get('key', '')
        return render_template('index.html', data=key)

# RESTful API Route
# 路由传参
class jsonAPI(Resource):
    # http://127.0.0.1:5000/api/值
    def get(self,key):
        try:
            jsonObj = {"result":key,'function':1}
            return jsonify(jsonObj)
        except Exception:
            return jsonify({"error":"error"})
    
    # http://127.0.0.1:5000/api/值
    # 传{"key":"值"}
    def post(self,key):
        try:
            args = parser.parse_args()
            key = eval(args['key'])
            jsonObj = {"result":key,'function':1}
            return jsonify(jsonObj)
        except Exception:
            return jsonify({"error":"error"})
api.add_resource(jsonAPI, '/api/<key>')

# 问号传参
class jsonAPI2(Resource):
    # http://127.0.0.1:5000/api/?pkg=值
    def get(self):
        try:
            pkg = request.args.get("pkg","")
            jsonObj = {"result":pkg,'function':2}
            return jsonify(jsonObj)
        except Exception:
            return jsonify({"error":"error"})
    
    # http://127.0.0.1:5000/api/
    # 传{"key":"值"}
    def post(self):
        try:
            args = parser.parse_args()
            key = eval(args['key'])
            jsonObj = {"result":key,'function':2}
            return jsonify(jsonObj)
        except Exception:
            return jsonify({"error":"error"})
api.add_resource(jsonAPI2, '/api/')

if __name__ == '__main__':
    app.run(debug=False)